package dev.iuredev.HelpDeskAPI.users.service;

import dev.iuredev.HelpDeskAPI.exceptions.BusinessException;
import dev.iuredev.HelpDeskAPI.exceptions.ResourceNotFoundException;
import dev.iuredev.HelpDeskAPI.users.dto.request.UserChangeStatusRequestDTO;
import dev.iuredev.HelpDeskAPI.users.dto.request.UserCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.users.dto.request.UserUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.users.dto.response.UserResponseDTO;
import dev.iuredev.HelpDeskAPI.users.mapper.UserMapper;
import dev.iuredev.HelpDeskAPI.users.model.UserModel;
import dev.iuredev.HelpDeskAPI.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<UserResponseDTO> findAllUsers() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public UserResponseDTO findUserById(Long id) {
        Optional<UserModel> userModel = repository.findById(id);
        return mapper.toDTO(userModel.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado.")));
    }

    public UserResponseDTO createUser(UserCreateRequestDTO requestDTO) {
        if (!repository.existsByEmailIgnoreCase(requestDTO.email().toLowerCase().trim())) {
            UserModel userModel = mapper.toEntity(requestDTO);
            userModel.setEmail(userModel.getEmail().toLowerCase().trim());
            UserModel savedModel = repository.save(userModel);
            return mapper.toDTO(savedModel);
        }
        throw new BusinessException("E-mail informado já existe no sistema");
    }

    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO requestDTO) {
        Optional<UserModel> userModel = repository.findById(id);
        if (userModel.isPresent()) {
            UserModel existingModel = userModel.get();
            if (!repository.existsByEmailIgnoreCaseAndIdNot(requestDTO.email().toLowerCase().trim(), id)) {
                mapper.updateEntity(requestDTO, existingModel);
                existingModel.setEmail(existingModel.getEmail().toLowerCase().trim());
                UserModel updateModel = repository.save(existingModel);
                return mapper.toDTO(updateModel);
            }
            throw new BusinessException("E-mail informado já existe no sistema");
        }
        throw new ResourceNotFoundException("Usuário não encontrado.");
    }

    public UserResponseDTO changeUserStatus(Long id, UserChangeStatusRequestDTO requestDTO) {
        Optional<UserModel> userModel = repository.findById(id);
        if (userModel.isPresent()) {
            UserModel existingModel = userModel.get();
            existingModel.setActive(requestDTO.active());
            UserModel savedModel = repository.save(existingModel);
            return mapper.toDTO(savedModel);
        }
        throw new ResourceNotFoundException("Usuário não encontrado");
    }

}
