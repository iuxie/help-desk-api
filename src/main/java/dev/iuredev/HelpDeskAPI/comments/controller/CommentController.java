package dev.iuredev.HelpDeskAPI.comments.controller;

import dev.iuredev.HelpDeskAPI.comments.dto.request.CommentCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.comments.dto.request.CommentUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.comments.dto.response.CommentResponseDTO;
import dev.iuredev.HelpDeskAPI.comments.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Comentários", description = "Gerenciamento dos comentários dos chamados")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping("/ticket/{ticketId}")
    @Operation(summary = "Listar comentários públicos de um chamado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comentários listados com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado")
    })
    public ResponseEntity<List<CommentResponseDTO>> findAllNotInternalCommentsInTicket(
            @Parameter(description = "ID do chamado", example = "1") @PathVariable Long ticketId) {
        return ResponseEntity.ok(service.findAllNotInternalCommentsInTicket(ticketId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar comentário por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comentário encontrado",
                    content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    })
    public ResponseEntity<CommentResponseDTO> findCommentById(
            @Parameter(description = "ID do comentário", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.findCommentById(id));
    }

    @PostMapping
    @Operation(summary = "Adicionar comentário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comentário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Chamado ou usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Comentário não permitido pela regra de negócio")
    })
    public ResponseEntity<CommentResponseDTO> createComment(
            @Valid @RequestBody CommentCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createComment(requestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar comentário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comentário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Comentário ou chamado não encontrado"),
            @ApiResponse(responseCode = "409", description = "Atualização não permitida pela regra de negócio")
    })
    public ResponseEntity<CommentResponseDTO> updateComment(
            @Parameter(description = "ID do comentário", example = "1") @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(service.updateComment(id, requestDTO));
    }

}
