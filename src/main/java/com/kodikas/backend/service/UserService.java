package com.kodikas.backend.service;

import com.kodikas.backend.dto.userDTO.*;
import com.kodikas.backend.model.Company;
import com.kodikas.backend.model.User;
import com.kodikas.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável por gerenciar as operações relacionadas aos usuários.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyService companyService;

    /**
     * Obtém todos os usuários ativos.
     *
     * @return Lista de DTOs de usuários ativos.
     */
    public List<ResponseListUsers> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(User::getAtivo)
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Obtém os detalhes de um usuário pelo ID.
     *
     * @param id ID do usuário.
     * @return DTO com os detalhes do usuário.
     * @throws IllegalArgumentException se o usuário não for encontrado.
     */
    public ResponseDetailUserDTO getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id)
        );

        logger.info("Usuário encontrado com ID: {}", id);
        return mapToDetailResponse(user);
    }

    /**
     * Cria um novo usuário.
     *
     * @param user Dados para criação do usuário.
     * @return DTO com os detalhes do usuário criado.
     */
    public ResponseCreateUserDTO createUser(DataCreateUserDTO user) {
        User newUser = mapToEntityCreate(user);
        User savedUser = userRepository.save(newUser);

        logger.info("Usuário criado com ID: {}", savedUser.getId());
        return mapToResponseCreate(savedUser);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id ID do usuário a ser atualizado.
     * @param userDetails Dados para atualização do usuário.
     * @return DTO com os detalhes do usuário atualizado.
     * @throws IllegalArgumentException se a empresa associada não for encontrada.
     */
    public ResponseDetailUserDTO updateUser(Long id, DataUpdateUser userDetails) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id)
        );

        if (userDetails.name() != null) {
            user.setName(userDetails.name());
        }
        if (userDetails.email() != null) {
            user.setEmail(userDetails.email());
        }

        if (userDetails.companyId() != null) {
            Company company = companyService.getCompany(userDetails.companyId());
            if (company == null) {
                throw new IllegalArgumentException("Empresa não encontrada com o ID: " + userDetails.companyId());
            }
            user.setCompany(company);
        }

        User updatedUser = userRepository.save(user);
        logger.info("Usuário atualizado com ID: {}", updatedUser.getId());
        return mapToDetailResponse(updatedUser);
    }

    /**
     * Exclui logicamente um usuário pelo ID.
     *
     * @param id ID do usuário a ser excluído.
     * @throws IllegalArgumentException se o usuário não for encontrado.
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id)
        );

        logger.info("Deletando usuário com ID: {}", id);
        user.setAtivo(false);
        userRepository.save(user);
        logger.info("Usuário deletado com ID: {}", id);
    }

    /**
     * Obtém um usuário pelo ID.
     *
     * @param id ID do usuário.
     * @return Entidade User ou null se não encontrado.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Mapeia uma entidade User para um DTO de lista.
     *
     * @param user Entidade User.
     * @return DTO de lista de usuários.
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
     * Mapeia uma entidade User para um DTO detalhado.
     *
     * @param user Entidade User.
     * @return DTO detalhado do usuário.
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
     * Mapeia uma entidade User para um DTO de criação.
     *
     * @param user Entidade User.
     * @return DTO de criação do usuário.
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
     * Mapeia os dados de criação para uma entidade User.
     *
     * @param dto Dados de criação do usuário.
     * @return Entidade User.
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