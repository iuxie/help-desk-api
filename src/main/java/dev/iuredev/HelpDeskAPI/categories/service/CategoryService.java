package dev.iuredev.HelpDeskAPI.categories.service;

import dev.iuredev.HelpDeskAPI.categories.dto.response.CategoryResponseDTO;
import dev.iuredev.HelpDeskAPI.categories.mapper.CategoryMapper;
import dev.iuredev.HelpDeskAPI.categories.model.CategoryModel;
import dev.iuredev.HelpDeskAPI.categories.repository.CategoryRepository;
import dev.iuredev.HelpDeskAPI.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CategoryResponseDTO> findAllActiveCategories() {
        return repository.findAllByActiveTrue().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<CategoryResponseDTO> findAllCategories() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public CategoryResponseDTO findCategoryById(Long id) {
        Optional<CategoryModel> categoryModel = repository.findById(id);
        return mapper.toDTO(categoryModel.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada.")));
    }

}
