package dev.iuredev.HelpDeskAPI.categories.controller;

import dev.iuredev.HelpDeskAPI.categories.dto.request.CategoryChangeStatusRequestDTO;
import dev.iuredev.HelpDeskAPI.categories.dto.request.CategoryRequestDTO;
import dev.iuredev.HelpDeskAPI.categories.dto.response.CategoryResponseDTO;
import dev.iuredev.HelpDeskAPI.categories.service.CategoryService;
import dev.iuredev.HelpDeskAPI.exceptions.BusinessException;
import dev.iuredev.HelpDeskAPI.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAllCategories() {
        return ResponseEntity.ok(service.findAllCategories());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponseDTO>> findAllActiveCategories() {
        return ResponseEntity.ok(service.findAllActiveCategories());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findCategoryById(@PathVariable Long id) {
        try {
            CategoryResponseDTO responseDTO = service.findCategoryById(id);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        try {
            CategoryResponseDTO responseDTO = service.createCategory(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(responseDTO);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeCategoryStatus(
            @PathVariable Long id,
            @Valid @RequestBody CategoryChangeStatusRequestDTO requestDTO) {
        try {
            CategoryResponseDTO responseDTO = service.changeCategoryStatus(id, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        try {
            CategoryResponseDTO responseDTO = service.updateCategory(id, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

}
