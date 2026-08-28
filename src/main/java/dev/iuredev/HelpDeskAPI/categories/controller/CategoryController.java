package dev.iuredev.HelpDeskAPI.categories.controller;

import dev.iuredev.HelpDeskAPI.categories.dto.request.CategoryChangeStatusRequestDTO;
import dev.iuredev.HelpDeskAPI.categories.dto.request.CategoryRequestDTO;
import dev.iuredev.HelpDeskAPI.categories.dto.response.CategoryResponseDTO;
import dev.iuredev.HelpDeskAPI.categories.service.CategoryService;
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
            return ResponseEntity.ok(service.findCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.createCategory(requestDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeCategoryStatus(
            @PathVariable Long id,
            @Valid @RequestBody CategoryChangeStatusRequestDTO requestDTO) {
            return ResponseEntity.ok(service.changeCategoryStatus(id, requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                            @Valid @RequestBody CategoryRequestDTO requestDTO) {
            return ResponseEntity.ok(service.updateCategory(id, requestDTO));
    }

}
