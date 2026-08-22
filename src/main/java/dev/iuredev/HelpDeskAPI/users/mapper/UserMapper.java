package dev.iuredev.HelpDeskAPI.users.mapper;

import dev.iuredev.HelpDeskAPI.users.dto.request.UserCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.users.dto.request.UserUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.users.dto.response.UserResponseDTO;
import dev.iuredev.HelpDeskAPI.users.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserModel toEntity(UserCreateRequestDTO userCreateRequestDTO);

    void updateEntity(
            UserUpdateRequestDTO userUpdateRequestDTO,
            @MappingTarget UserModel userModel
    );

    UserResponseDTO toDTO(UserModel userModel);

}
