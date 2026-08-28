package dev.iuredev.HelpDeskAPI.comments.controller;

import dev.iuredev.HelpDeskAPI.comments.dto.request.CommentCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.comments.dto.request.CommentUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.comments.dto.response.CommentResponseDTO;
import dev.iuredev.HelpDeskAPI.comments.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<CommentResponseDTO>> findAllNotInternalCommentsInTicket(
            @PathVariable Long ticketId) {
        return ResponseEntity.ok(service.findAllNotInternalCommentsInTicket(ticketId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> findCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findCommentById(id));
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(
            @Valid @RequestBody CommentCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createComment(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(service.updateComment(id, requestDTO));
    }

}
