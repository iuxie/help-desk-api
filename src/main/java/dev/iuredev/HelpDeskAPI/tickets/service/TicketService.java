package dev.iuredev.HelpDeskAPI.tickets.service;

import dev.iuredev.HelpDeskAPI.categories.model.CategoryModel;
import dev.iuredev.HelpDeskAPI.categories.repository.CategoryRepository;
import dev.iuredev.HelpDeskAPI.enums.Priority;
import dev.iuredev.HelpDeskAPI.enums.TicketStatus;
import dev.iuredev.HelpDeskAPI.exceptions.BusinessException;
import dev.iuredev.HelpDeskAPI.exceptions.ResourceNotFoundException;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketUpdateRequestDTO;
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
        UserModel userModel = validateUser(requestDTO.requesterId());
        CategoryModel categoryModel = validateCategory(requestDTO.categoryId());
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
        CategoryModel categoryModel = validateCategory(requestDTO.categoryId());
        TicketModel ticketModel = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        mapper.updateEntity(requestDTO, ticketModel);
        ticketModel.setCategory(categoryModel);
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

    private CategoryModel validateCategory(Long id) {
        CategoryModel categoryModel = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));

        if (!Boolean.TRUE.equals(categoryModel.getActive())) {
            throw new BusinessException("Categoria inativa.");
        }

        return categoryModel;
    }

    private UserModel validateUser(Long id) {
        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (!Boolean.TRUE.equals(userModel.getActive())) {
            throw new BusinessException("Usuário inativo.");
        }

        return userModel;
    }

}
