package dev.iuredev.HelpDeskAPI.categories.service;

import dev.iuredev.HelpDeskAPI.categories.dto.request.CategoryChangeStatusRequestDTO;
import dev.iuredev.HelpDeskAPI.categories.dto.request.CategoryRequestDTO;
import dev.iuredev.HelpDeskAPI.categories.dto.response.CategoryResponseDTO;
import dev.iuredev.HelpDeskAPI.categories.mapper.CategoryMapper;
import dev.iuredev.HelpDeskAPI.categories.model.CategoryModel;
import dev.iuredev.HelpDeskAPI.categories.repository.CategoryRepository;
import dev.iuredev.HelpDeskAPI.exceptions.BusinessException;
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

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        String normalizedName = requestDTO.name()
                .trim()
                .toUpperCase();
        if (!repository.existsByNameIgnoreCase(normalizedName)) {
            CategoryModel categoryModel = mapper.toEntity(requestDTO);
            categoryModel.setName(normalizedName);
            CategoryModel savedModel = repository.save(categoryModel);
            return mapper.toDTO(savedModel);
        }
        throw new BusinessException("Categoria com o nome informado já existe no sistema");
    }

    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        String normalizedName = requestDTO.name()
                .trim()
                .toUpperCase();
        Optional<CategoryModel> categoryModel = repository.findById(id);
        if (categoryModel.isPresent()) {
            CategoryModel existingCategory = categoryModel.get();
            if (!repository.existsByNameIgnoreCaseAndIdNot(normalizedName, id)) {
                mapper.updateEntity(requestDTO, existingCategory);
                existingCategory.setName(normalizedName);
                CategoryModel savedModel = repository.save(existingCategory);
                return mapper.toDTO(savedModel);
            }
            throw new BusinessException("Categoria com o nome informado já existe no sistema");
        }
        throw new ResourceNotFoundException("Categoria não encontrada.");
    }

    @Transactional
    public CategoryResponseDTO changeCategoryStatus(Long id, CategoryChangeStatusRequestDTO requestDTO) {
        Optional<CategoryModel> categoryModel = repository.findById(id);
        if (categoryModel.isPresent()) {
            CategoryModel existingCategory = categoryModel.get();
            existingCategory.setActive(requestDTO.active());
            CategoryModel savedModel = repository.save(existingCategory);
            return mapper.toDTO(savedModel);
        }
        throw new ResourceNotFoundException("Categoria não encontrada.");
    }

}
