package dev.iuredev.HelpDeskAPI.tickets.mapper;

import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.response.TicketResponseDTO;
import dev.iuredev.HelpDeskAPI.tickets.model.TicketModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "solution", ignore = true)
    @Mapping(target = "openedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "slaDeadline", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "technician", ignore = true)
    @Mapping(target = "category", ignore = true)
    TicketModel toEntity(TicketCreateRequestDTO request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "solution", ignore = true)
    @Mapping(target = "openedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "slaDeadline", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "technician", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntity(
            TicketUpdateRequestDTO request,
            @MappingTarget TicketModel ticket
    );

    @Mapping(target = "requesterId", source = "requester.id")
    @Mapping(target = "technicianId", source = "technician.id")
    @Mapping(target = "categoryId", source = "category.id")
    TicketResponseDTO toDTO(TicketModel ticket);
}