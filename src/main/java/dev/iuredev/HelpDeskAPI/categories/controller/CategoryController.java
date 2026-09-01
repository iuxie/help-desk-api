package dev.iuredev.HelpDeskAPI.categories.controller;

import dev.iuredev.HelpDeskAPI.categories.dto.request.CategoryChangeStatusRequestDTO;
import dev.iuredev.HelpDeskAPI.categories.dto.request.CategoryRequestDTO;
import dev.iuredev.HelpDeskAPI.categories.dto.response.CategoryResponseDTO;
import dev.iuredev.HelpDeskAPI.categories.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categorias", description = "Gerenciamento das categorias de chamados")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar categorias")
    @ApiResponse(responseCode = "200", description = "Categorias listadas com sucesso",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class))))
    public ResponseEntity<List<CategoryResponseDTO>> findAllCategories() {
        return ResponseEntity.ok(service.findAllCategories());
    }

    @GetMapping("/active")
    @Operation(summary = "Listar categorias ativas")
    @ApiResponse(responseCode = "200", description = "Categorias ativas listadas com sucesso",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class))))
    public ResponseEntity<List<CategoryResponseDTO>> findAllActiveCategories() {
        return ResponseEntity.ok(service.findAllActiveCategories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<CategoryResponseDTO> findCategoryById(
            @Parameter(description = "ID da categoria", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.findCategoryById(id));
    }

    @PostMapping
    @Operation(summary = "Criar categoria")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Nome da categoria já cadastrado")
    })
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createCategory(requestDTO));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Ativar ou inativar categoria")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<CategoryResponseDTO> changeCategoryStatus(
            @Parameter(description = "ID da categoria", example = "1") @PathVariable Long id,
            @Valid @RequestBody CategoryChangeStatusRequestDTO requestDTO) {
        return ResponseEntity.ok(service.changeCategoryStatus(id, requestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "409", description = "Nome da categoria já cadastrado")
    })
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @Parameter(description = "ID da categoria", example = "1") @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        return ResponseEntity.ok(service.updateCategory(id, requestDTO));
    }

}
