package dev.iuredev.HelpDeskAPI.comments.service;

import dev.iuredev.HelpDeskAPI.comments.dto.response.CommentResponseDTO;
import dev.iuredev.HelpDeskAPI.comments.mapper.CommentMapper;
import dev.iuredev.HelpDeskAPI.comments.repository.CommentRepository;
import dev.iuredev.HelpDeskAPI.exceptions.ResourceNotFoundException;
import dev.iuredev.HelpDeskAPI.tickets.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final TicketRepository ticketRepository;

    public CommentService(CommentRepository repository, CommentMapper mapper, TicketRepository ticketRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.ticketRepository = ticketRepository;
    }

    public List<CommentResponseDTO> findAllNotInternalCommentsInTicket(Long ticketId) {
        if (ticketRepository.existsById(ticketId)) {
            return repository.findAllByTicketIdAndInternalFalseOrderByCreatedAtAsc(ticketId).stream()
                    .map(mapper::toDTO)
                    .toList();
        }
        throw new ResourceNotFoundException("Chamado não encontrado.");
    }

    public CommentResponseDTO findCommentById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado.")));
    }

}
