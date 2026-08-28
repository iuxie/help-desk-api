package dev.iuredev.HelpDeskAPI.tickets.controller;

import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketAssignmentRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketResolveRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketStatusUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.response.TicketResponseDTO;
import dev.iuredev.HelpDeskAPI.tickets.service.TicketService;
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
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> findAllTickets() {
        return ResponseEntity.ok(service.findAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> findTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findTicketById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<TicketResponseDTO> findTicketByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.findTicketByCode(code));
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(
            @Valid @RequestBody TicketCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createTicket(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(service.updateTicket(id, requestDTO));
    }

    @PatchMapping("/{id}/assignment")
    public ResponseEntity<TicketResponseDTO> assignTechnician(
            @PathVariable Long id,
            @Valid @RequestBody TicketAssignmentRequestDTO requestDTO) {
        return ResponseEntity.ok(service.assignTechnician(id, requestDTO));
    }

    @PatchMapping("/{id}/resolution")
    public ResponseEntity<TicketResponseDTO> resolveTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketResolveRequestDTO requestDTO) {
        return ResponseEntity.ok(service.resolveTicket(id, requestDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponseDTO> changeTicketStatus(
            @PathVariable Long id,
            @Valid @RequestBody TicketStatusUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(service.changeTicketStatus(id, requestDTO));
    }

}
