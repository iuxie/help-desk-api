package dev.iuredev.HelpDeskAPI.comments.mapper;

import dev.iuredev.HelpDeskAPI.comments.dto.request.CommentCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.comments.dto.request.CommentUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.comments.dto.response.CommentResponseDTO;
import dev.iuredev.HelpDeskAPI.comments.model.CommentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    CommentModel toEntity(CommentCreateRequestDTO request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    void updateEntity(
            CommentUpdateRequestDTO request,
            @MappingTarget CommentModel comment
    );

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "ticketId", source = "ticket.id")
    CommentResponseDTO toDTO(CommentModel comment);
}