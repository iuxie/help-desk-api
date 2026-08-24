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

        TicketStatus requestStatus = requestDTO.status();
        TicketStatus ticketStatus = ticketModel.getStatus();

        String statusName =  (ticketStatus.equals(TicketStatus.RESOLVIDO)) ? "RESOLVIDO" :
                ticketStatus.equals(TicketStatus.CANCELADO) ? "CANCELADO" :
                ticketStatus.equals(TicketStatus.EM_ATENDIMENTO) ? "EM_ATENDIMENTO" :
                    ticketStatus.equals(TicketStatus.AGUARDANDO_SOLICITANTE) ? "AGUARDANDO_SOLICITANTE" : "ABERTO";

        if (ticketStatus.equals(TicketStatus.RESOLVIDO) || ticketStatus.equals(TicketStatus.CANCELADO)) {
            throw new BusinessException("Chamado " + statusName + " não pode ter o status alterado.");
        }
        if (ticketStatus.equals(TicketStatus.ABERTO)) {
            if (requestStatus.equals(TicketStatus.RESOLVIDO)) {
                throw new BusinessException("Chamado ABERTO não pode ser resolvido");
            }
        }
        if (ticketStatus.equals(TicketStatus.EM_ATENDIMENTO) || ticketStatus.equals(TicketStatus.AGUARDANDO_SOLICITANTE)) {
            if (requestStatus.equals(TicketStatus.ABERTO)) {
                throw new BusinessException("Chamado EM_ATENDIMENTO não pode ser aberto novamente.");
            }
        }

        ticketModel.setStatus(requestStatus);
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
