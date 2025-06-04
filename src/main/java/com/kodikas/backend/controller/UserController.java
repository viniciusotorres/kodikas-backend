package com.kodikas.backend.controller;

import com.kodikas.backend.constants.ApiPaths;
import com.kodikas.backend.dto.userDTO.*;
import com.kodikas.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas aos usuários.
 */
@RestController
@RequestMapping(ApiPaths.API_V1 + "/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Retorna todos os usuários ativos.
     *
     * @return ResponseEntity contendo a lista de usuários ativos.
     */
    @GetMapping("/list")
    public ResponseEntity<List<ResponseListUsers>> getAllUsers() {
        try {
            List<ResponseListUsers> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Erro ao buscar usuários: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retorna os detalhes de um usuário pelo ID.
     *
     * @param id ID do usuário.
     * @return ResponseEntity contendo os detalhes do usuário.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDetailUserDTO> getUserId(@PathVariable Long id) {
        try {
            ResponseDetailUserDTO user = userService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException e) {
            logger.error("Usuário não encontrado com ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Erro ao buscar usuário: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cria um novo usuário.
     *
     * @param user Dados para criação do usuário.
     * @return ResponseEntity contendo os detalhes do usuário criado.
     */
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<ResponseCreateUserDTO> createUser(@RequestBody DataCreateUserDTO user) {
        try {
            ResponseCreateUserDTO createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (EntityNotFoundException e) {
            logger.error("Usuário não encontrado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Erro ao criar usuário: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id ID do usuário a ser atualizado.
     * @param user Dados para atualização do usuário.
     * @return ResponseEntity contendo os detalhes do usuário atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDetailUserDTO> updateUser(@PathVariable Long id, @RequestBody DataUpdateUser user) {
        try {
            ResponseDetailUserDTO updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (EntityNotFoundException e) {
            logger.error("Usuário não encontrado com ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exclui logicamente um usuário pelo ID.
     *
     * @param id ID do usuário a ser excluído.
     * @return ResponseEntity vazio indicando o sucesso ou falha da operação.
     */
    @PutMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.error("Usuário não encontrado com ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Erro ao excluir usuário: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}