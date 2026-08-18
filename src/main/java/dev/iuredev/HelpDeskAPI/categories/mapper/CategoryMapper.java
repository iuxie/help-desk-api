package dev.iuredev.HelpDeskAPI.categories.mapper;

import dev.iuredev.HelpDeskAPI.categories.dto.request.CategoryRequestDTO;
import dev.iuredev.HelpDeskAPI.categories.dto.response.CategoryResponseDTO;
import dev.iuredev.HelpDeskAPI.categories.model.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryModel toEntity(CategoryRequestDTO categoryRequestDTO);

    void updateEntity(
            CategoryRequestDTO categoryRequestDTO,
            @MappingTarget CategoryModel categoryModel
    );

    CategoryResponseDTO toDTO(CategoryModel categoryModel);

}
