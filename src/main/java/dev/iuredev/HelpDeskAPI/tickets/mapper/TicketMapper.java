package dev.iuredev.HelpDeskAPI.tickets.mapper;

import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.request.TicketUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.tickets.dto.response.TicketResponseDTO;
import dev.iuredev.HelpDeskAPI.tickets.model.TicketModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketModel toEntity(TicketCreateRequestDTO ticketCreateRequestDTO);

    void updateEntity(
            TicketUpdateRequestDTO ticketUpdateRequestDTO,
            @MappingTarget TicketModel ticketModel
    );

    TicketResponseDTO toDTO(TicketModel ticketModel);

}
