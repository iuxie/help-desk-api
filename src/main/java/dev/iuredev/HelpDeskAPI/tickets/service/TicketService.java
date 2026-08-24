package dev.iuredev.HelpDeskAPI.tickets.service;

import dev.iuredev.HelpDeskAPI.categories.model.CategoryModel;
import dev.iuredev.HelpDeskAPI.categories.repository.CategoryRepository;
import dev.iuredev.HelpDeskAPI.enums.Priority;
import dev.iuredev.HelpDeskAPI.enums.Role;
import dev.iuredev.HelpDeskAPI.enums.TicketStatus;
import dev.iuredev.HelpDeskAPI.exceptions.BusinessException;
import dev.iuredev.HelpDeskAPI.exceptions.ResourceNotFoundException;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.*;
import dev.iuredev.HelpDeskAPI.tickets.dto.response.TicketResponseDTO;
import dev.iuredev.HelpDeskAPI.tickets.mapper.TicketMapper;
import dev.iuredev.HelpDeskAPI.tickets.model.TicketModel;
import dev.iuredev.HelpDeskAPI.tickets.repository.TicketRepository;
import dev.iuredev.HelpDeskAPI.users.model.UserModel;
import dev.iuredev.HelpDeskAPI.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository repository;
    private final TicketMapper mapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TicketService(TicketRepository repository, TicketMapper mapper, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<TicketResponseDTO> findAllTickets() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public TicketResponseDTO findTicketById(Long id) {
        Optional<TicketModel> ticket = repository.findById(id);
        return mapper.toDTO(ticket.orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado.")));
    }

    public TicketResponseDTO findTicketByCode(String code) {
        Optional<TicketModel> ticket = repository.findByCode(code);
        return mapper.toDTO(ticket.orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado.")));
    }

    @Transactional
    public TicketResponseDTO createTicket(TicketCreateRequestDTO requestDTO) {
        UserModel userModel = findActiveUser(requestDTO.requesterId());
        CategoryModel categoryModel = findActiveCategory(requestDTO.categoryId());
        TicketModel ticketModel = mapper.toEntity(requestDTO);
        ticketModel.setCode(generateCode());
        ticketModel.setStatus(TicketStatus.ABERTO);
        ticketModel.setRequester(userModel);
        ticketModel.setCategory(categoryModel);
        ticketModel.setSlaDeadline(generateSlaDeadline(ticketModel.getPriority()));
        TicketModel savedModel = repository.save(ticketModel);
        return mapper.toDTO(savedModel);
    }

    @Transactional
    public TicketResponseDTO updateTicket(Long id, TicketUpdateRequestDTO requestDTO) {
        TicketModel ticketModel = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado."));
        CategoryModel categoryModel = findActiveCategory(requestDTO.categoryId());
        mapper.updateEntity(requestDTO, ticketModel);
        ticketModel.setCategory(categoryModel);
        ticketModel.setSlaDeadline(ticketModel.getOpenedAt().plusHours(ticketModel.getPriority().getSlaHours()));
        TicketModel savedModel = repository.save(ticketModel);
        return mapper.toDTO(savedModel);
    }

    @Transactional
    public TicketResponseDTO assignTechnician(Long id, TicketAssignmentRequestDTO requestDTO) {
        TicketModel ticketModel = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado."));

        if (ticketModel.getStatus() == TicketStatus.RESOLVIDO) {
            throw new BusinessException("Chamado resolvido.");
        }
        if (ticketModel.getStatus() == TicketStatus.CANCELADO) {
            throw new BusinessException("Chamado cancelado.");
        }

        UserModel userModel = findActiveUser(requestDTO.technicianId());

        if (userModel.getRole() == Role.TECNICO) {
            ticketModel.setTechnician(userModel);
            ticketModel.setStatus(TicketStatus.EM_ATENDIMENTO);
            TicketModel savedModel = repository.save(ticketModel);
            return mapper.toDTO(savedModel);
        }
        throw new BusinessException("Usuário informado não é um TECNICO.");
    }

    @Transactional
    public TicketResponseDTO resolveTicket(Long id, TicketResolveRequestDTO requestDTO) {
        TicketModel ticketModel = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado."));

        if (ticketModel.getStatus() == TicketStatus.CANCELADO) {
            throw new BusinessException("Chamado cancelado não pode ser resolvido.");
        }
        if (ticketModel.getStatus() == TicketStatus.ABERTO) {
                throw new BusinessException("Chamado aberto não pode ser resolvido.");
        }
        if (ticketModel.getStatus() == TicketStatus.RESOLVIDO) {
            throw new BusinessException("Chamado resolvido não pode ser resolvido novamente.");
        }
        if (ticketModel.getTechnician() == null) {
            throw new BusinessException("Chamado sem técnico.");
        }

        ticketModel.setSolution(requestDTO.solution());
        ticketModel.setStatus(TicketStatus.RESOLVIDO);
        ticketModel.setResolvedAt(LocalDateTime.now());
        TicketModel savedModel = repository.save(ticketModel);
        return mapper.toDTO(savedModel);
    }

    @Transactional
    public TicketResponseDTO changeTicketStatus(Long id, TicketStatusUpdateRequestDTO requestDTO) {
        TicketModel ticketModel = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado."));

        TicketStatus currentStatus = ticketModel.getStatus();
        TicketStatus requestedStatus = requestDTO.status();

        if (currentStatus == TicketStatus.RESOLVIDO || currentStatus == TicketStatus.CANCELADO) {
            throw new BusinessException(
                    "Chamado " + currentStatus.name() + " não pode ter o status alterado."
            );
        }

        if (requestedStatus == TicketStatus.RESOLVIDO) {
            throw new BusinessException(
                    "Utilize a operação de resolução para definir o chamado como RESOLVIDO."
            );
        }

        boolean validTransition = switch (currentStatus) {
            case ABERTO -> requestedStatus == TicketStatus.CANCELADO;
            case EM_ATENDIMENTO -> requestedStatus == TicketStatus.AGUARDANDO_SOLICITANTE
                    || requestedStatus == TicketStatus.CANCELADO;
            case AGUARDANDO_SOLICITANTE -> requestedStatus == TicketStatus.EM_ATENDIMENTO
                    || requestedStatus == TicketStatus.CANCELADO;
            case RESOLVIDO, CANCELADO -> false;
        };

        if (!validTransition) {
            throw new BusinessException(
                    "Transição de status inválida: " + currentStatus.name()
                            + " para " + requestedStatus.name() + "."
            );
        }

        if ((requestedStatus == TicketStatus.EM_ATENDIMENTO
                || requestedStatus == TicketStatus.AGUARDANDO_SOLICITANTE)
                && ticketModel.getTechnician() == null) {
            throw new BusinessException(
                    "Chamado precisa ter um técnico atribuído para assumir o status "
                            + requestedStatus.name() + "."
            );
        }

        ticketModel.setStatus(requestedStatus);
        TicketModel savedModel = repository.save(ticketModel);
        return mapper.toDTO(savedModel);
    }

    private String generateCode() {
        return "TKT-" + UUID.randomUUID()
                .toString()
                .toUpperCase(Locale.ROOT);
    }

    private LocalDateTime generateSlaDeadline(Priority priority) {
        return LocalDateTime.now().plusHours(priority.getSlaHours());
    }

    private CategoryModel findActiveCategory(Long id) {
        CategoryModel categoryModel = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));

        if (!Boolean.TRUE.equals(categoryModel.getActive())) {
            throw new BusinessException("Categoria inativa.");
        }

        return categoryModel;
    }

    private UserModel findActiveUser(Long id) {
        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (!Boolean.TRUE.equals(userModel.getActive())) {
            throw new BusinessException("Usuário inativo.");
        }

        return userModel;
    }

}
