package dev.iuredev.HelpDeskAPI.users.controller;

import dev.iuredev.HelpDeskAPI.users.dto.request.UserChangeStatusRequestDTO;
import dev.iuredev.HelpDeskAPI.users.dto.request.UserCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.users.dto.request.UserUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.users.dto.response.UserResponseDTO;
import dev.iuredev.HelpDeskAPI.users.service.UserService;
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
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Gerenciamento dos usuários do sistema")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar usuários")
    @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))))
    public ResponseEntity<List<UserResponseDTO>> findAllUsers() {
        return ResponseEntity.ok(service.findAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UserResponseDTO> findUserById(
            @Parameter(description = "ID do usuário", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.findUserById(id));
    }

    @PostMapping
    @Operation(summary = "Criar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    })
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createUser(requestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    })
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID do usuário", example = "1") @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(service.updateUser(id, requestDTO));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Ativar ou inativar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UserResponseDTO> changeUserStatus(
            @Parameter(description = "ID do usuário", example = "1") @PathVariable Long id,
            @Valid @RequestBody UserChangeStatusRequestDTO requestDTO) {
        return ResponseEntity.ok(service.changeUserStatus(id, requestDTO));
    }

}
