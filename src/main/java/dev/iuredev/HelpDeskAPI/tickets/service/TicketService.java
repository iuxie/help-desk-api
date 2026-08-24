package dev.iuredev.HelpDeskAPI.tickets.service;

import dev.iuredev.HelpDeskAPI.categories.model.CategoryModel;
import dev.iuredev.HelpDeskAPI.categories.repository.CategoryRepository;
import dev.iuredev.HelpDeskAPI.enums.Priority;
import dev.iuredev.HelpDeskAPI.enums.TicketStatus;
import dev.iuredev.HelpDeskAPI.exceptions.ResourceNotFoundException;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketCreateRequestDTO;
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
        Optional<UserModel> userModel = userRepository.findById(requestDTO.requesterId());
        Optional<CategoryModel> categoryModel = categoryRepository.findById(requestDTO.categoryId());
        if (userModel.isPresent() && userModel.get().getActive() == true) {
            if (categoryModel.isPresent() && categoryModel.get().getActive() == true) {
                TicketModel ticketModel = mapper.toEntity(requestDTO);
                ticketModel.setCode(generateCode());
                ticketModel.setStatus(TicketStatus.ABERTO);
                ticketModel.setRequester(userModel.get());
                ticketModel.setCategory(categoryModel.get());
                ticketModel.setSlaDeadline(generateSlaDeadline(ticketModel.getPriority()));
                TicketModel savedModel = repository.save(ticketModel);
                return mapper.toDTO(savedModel);
            }
            throw new ResourceNotFoundException("Categoria não encontrada ou inativa.");
        }
        throw new ResourceNotFoundException("Usuário não encontrado ou inativo.");
    }

    private String generateCode() {
        return "TKT-" + UUID.randomUUID()
                .toString()
                .toUpperCase(Locale.ROOT);
    }

    private LocalDateTime generateSlaDeadline(Priority priority) {
        return LocalDateTime.now().plusHours(priority.getSlaHours());
    }

}
