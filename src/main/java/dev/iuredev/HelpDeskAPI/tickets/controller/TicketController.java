package dev.iuredev.HelpDeskAPI.tickets.controller;

import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketAssignmentRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketResolveRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketStatusUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.response.TicketResponseDTO;
import dev.iuredev.HelpDeskAPI.tickets.service.TicketService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Chamados", description = "Abertura e gerenciamento dos chamados de suporte")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar chamados")
    @ApiResponse(responseCode = "200", description = "Chamados listados com sucesso",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketResponseDTO.class))))
    public ResponseEntity<List<TicketResponseDTO>> findAllTickets() {
        return ResponseEntity.ok(service.findAllTickets());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar chamado por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chamado encontrado",
                    content = @Content(schema = @Schema(implementation = TicketResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado")
    })
    public ResponseEntity<TicketResponseDTO> findTicketById(
            @Parameter(description = "ID do chamado", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.findTicketById(id));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Buscar chamado pelo código")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chamado encontrado",
                    content = @Content(schema = @Schema(implementation = TicketResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado")
    })
    public ResponseEntity<TicketResponseDTO> findTicketByCode(
            @Parameter(description = "Código único do chamado", example = "TKT-550E8400-E29B-41D4-A716-446655440000")
            @PathVariable String code) {
        return ResponseEntity.ok(service.findTicketByCode(code));
    }

    @PostMapping
    @Operation(summary = "Abrir chamado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Chamado criado com sucesso",
                    content = @Content(schema = @Schema(implementation = TicketResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário ou categoria não encontrado"),
            @ApiResponse(responseCode = "409", description = "Usuário ou categoria inativo")
    })
    public ResponseEntity<TicketResponseDTO> createTicket(
            @Valid @RequestBody TicketCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createTicket(requestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar chamado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chamado atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = TicketResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Chamado ou categoria não encontrado"),
            @ApiResponse(responseCode = "409", description = "Categoria inativa")
    })
    public ResponseEntity<TicketResponseDTO> updateTicket(
            @Parameter(description = "ID do chamado", example = "1") @PathVariable Long id,
            @Valid @RequestBody TicketUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(service.updateTicket(id, requestDTO));
    }

    @PatchMapping("/{id}/assignment")
    @Operation(summary = "Atribuir técnico ao chamado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Técnico atribuído com sucesso",
                    content = @Content(schema = @Schema(implementation = TicketResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Chamado ou usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Atribuição não permitida pela regra de negócio")
    })
    public ResponseEntity<TicketResponseDTO> assignTechnician(
            @Parameter(description = "ID do chamado", example = "1") @PathVariable Long id,
            @Valid @RequestBody TicketAssignmentRequestDTO requestDTO) {
        return ResponseEntity.ok(service.assignTechnician(id, requestDTO));
    }

    @PatchMapping("/{id}/resolution")
    @Operation(summary = "Resolver chamado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chamado resolvido com sucesso",
                    content = @Content(schema = @Schema(implementation = TicketResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado"),
            @ApiResponse(responseCode = "409", description = "Resolução não permitida pela regra de negócio")
    })
    public ResponseEntity<TicketResponseDTO> resolveTicket(
            @Parameter(description = "ID do chamado", example = "1") @PathVariable Long id,
            @Valid @RequestBody TicketResolveRequestDTO requestDTO) {
        return ResponseEntity.ok(service.resolveTicket(id, requestDTO));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status do chamado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso",
                    content = @Content(schema = @Schema(implementation = TicketResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado"),
            @ApiResponse(responseCode = "409", description = "Transição de status não permitida")
    })
    public ResponseEntity<TicketResponseDTO> changeTicketStatus(
            @Parameter(description = "ID do chamado", example = "1") @PathVariable Long id,
            @Valid @RequestBody TicketStatusUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(service.changeTicketStatus(id, requestDTO));
    }

}
