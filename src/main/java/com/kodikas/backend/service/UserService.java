package com.kodikas.backend.service;

import com.kodikas.backend.dto.userDTO.*;
import com.kodikas.backend.exception.UserNotFoundException;
import com.kodikas.backend.model.Company;
import com.kodikas.backend.model.User;
import com.kodikas.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável por gerenciar as operações relacionadas aos usuários.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final CompanyService companyService;

    /**
     * Construtor para injetar dependências.
     *
     * @param userRepository Repositório de usuários.
     * @param companyService Serviço de empresas.
     */
    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    /**
     * Retorna todos os usuários ativos.
     *
     * @return Lista de usuários ativos.
     */
    public List<ResponseListUsers> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(User::getAtivo)
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Retorna os detalhes de um usuário pelo ID.
     *
     * @param id ID do usuário.
     * @return Detalhes do usuário.
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    public ResponseDetailUserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        logger.info("Usuário encontrado com ID: {}", id);
        return mapToDetailResponse(user);
    }

    /**
     * Cria um novo usuário.
     *
     * @param dto Dados para criação do usuário.
     * @return Detalhes do usuário criado.
     */
    public ResponseCreateUserDTO createUser(DataCreateUserDTO dto) {
        User newUser = mapToEntityCreate(dto);
        User savedUser = userRepository.save(newUser);

        logger.info("Usuário criado com ID: {}", savedUser.getId());
        return mapToResponseCreate(savedUser);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id  ID do usuário a ser atualizado.
     * @param dto Dados para atualização do usuário.
     * @return Detalhes do usuário atualizado.
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    public ResponseDetailUserDTO updateUser(Long id, DataUpdateUser dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.updateFrom(dto, companyService);
        User updatedUser = userRepository.save(user);

        logger.info("Usuário atualizado com ID: {}", updatedUser.getId());
        return mapToDetailResponse(updatedUser);
    }

    /**
     * Exclui logicamente um usuário pelo ID.
     *
     * @param id ID do usuário a ser excluído.
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        logger.info("Deletando usuário com ID: {}", id);
        user.setAtivo(false);
        userRepository.save(user);
        logger.info("Usuário deletado com ID: {}", id);
    }

    /**
     * Obtém um usuário pelo ID.
     *
     * @param id ID do usuário.
     * @return Entidade do usuário.
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Mapeia um usuário para um DTO de resposta de lista.
     *
     * @param user Entidade do usuário.
     * @return DTO de resposta de lista.
     */
    private ResponseListUsers mapToResponse(User user) {
        return new ResponseListUsers(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAtivo(),
                user.getCompany() != null ? user.getCompany().getId() : null
        );
    }

    /**
     * Mapeia um usuário para um DTO de resposta detalhada.
     *
     * @param user Entidade do usuário.
     * @return DTO de resposta detalhada.
     */
    private ResponseDetailUserDTO mapToDetailResponse(User user) {
        return new ResponseDetailUserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAtivo(),
                user.getCompany() != null ? user.getCompany().getId() : null,
                user.getCreatedAt() != null ? user.getCreatedAt().toString() : null
        );
    }

    /**
     * Mapeia um usuário para um DTO de resposta de criação.
     *
     * @param user Entidade do usuário.
     * @return DTO de resposta de criação.
     */
    private ResponseCreateUserDTO mapToResponseCreate(User user) {
        return new ResponseCreateUserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAtivo(),
                user.getCompany() != null ? user.getCompany().getId() : null,
                user.getCreatedAt() != null ? user.getCreatedAt().toString() : null
        );
    }

    /**
     * Mapeia um DTO de criação para uma entidade de usuário.
     *
     * @param dto DTO de criação.
     * @return Entidade de usuário.
     */
    private User mapToEntityCreate(DataCreateUserDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setAtivo(true);
        user.setCreatedAt(java.time.LocalDateTime.now());
        user.setCompany(null);
        return user;
    }
}