package dev.iuredev.HelpDeskAPI.comments.service;

import dev.iuredev.HelpDeskAPI.comments.dto.request.CommentCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.comments.dto.request.CommentUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.comments.dto.response.CommentResponseDTO;
import dev.iuredev.HelpDeskAPI.comments.mapper.CommentMapper;
import dev.iuredev.HelpDeskAPI.comments.model.CommentModel;
import dev.iuredev.HelpDeskAPI.comments.repository.CommentRepository;
import dev.iuredev.HelpDeskAPI.enums.Role;
import dev.iuredev.HelpDeskAPI.enums.TicketStatus;
import dev.iuredev.HelpDeskAPI.exceptions.BusinessException;
import dev.iuredev.HelpDeskAPI.exceptions.ResourceNotFoundException;
import dev.iuredev.HelpDeskAPI.tickets.model.TicketModel;
import dev.iuredev.HelpDeskAPI.tickets.repository.TicketRepository;
import dev.iuredev.HelpDeskAPI.users.model.UserModel;
import dev.iuredev.HelpDeskAPI.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository repository, CommentMapper mapper,
                          TicketRepository ticketRepository, UserRepository userRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
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

    @Transactional
    public CommentResponseDTO createComment(CommentCreateRequestDTO requestDTO) {
        TicketModel ticketModel = ticketRepository.findById(requestDTO.ticketId())
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado."));
        UserModel userModel = userRepository.findById(requestDTO.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        TicketStatus currentStatus = ticketModel.getStatus();
        Boolean internalComment = requestDTO.internal();
        Role currentRole = userModel.getRole();

        if (currentStatus.equals(TicketStatus.RESOLVIDO) || currentStatus.equals(TicketStatus.CANCELADO)) {
            throw new BusinessException("Chamado com status " + currentStatus + " não pode receber comentários.");
        }

        if (internalComment) {
            if (currentRole.equals(Role.SOLICITANTE)) {
                throw new BusinessException("Somente Técnicos ou Admins podem comentar internamente.");
            }
        }

        if (!userModel.getActive()) {
            throw new BusinessException("Usuário inativo.");
        }

        CommentModel commentModel = mapper.toEntity(requestDTO);
        commentModel.setTicket(ticketModel);
        commentModel.setAuthor(userModel);
        CommentModel savedModel = repository.save(commentModel);
        return mapper.toDTO(savedModel);
    }

    @Transactional
    public CommentResponseDTO updateComment(Long commentId, CommentUpdateRequestDTO requestDTO) {
        CommentModel commentModel = repository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado"));
        TicketModel ticketModel = ticketRepository.findById(commentModel.getTicket().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado"));

        TicketStatus currentStatus = ticketModel.getStatus();

        if (currentStatus == TicketStatus.RESOLVIDO || currentStatus == TicketStatus.CANCELADO) {
            throw new BusinessException("Chamado com status " + currentStatus + " não permite editar comentários.");
        }

        mapper.updateEntity(requestDTO, commentModel);
        return mapper.toDTO(commentModel);
    }

}
