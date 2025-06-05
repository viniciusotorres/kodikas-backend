package com.kodikas.backend.controller;

import com.kodikas.backend.constants.ApiPaths;
import com.kodikas.backend.dto.errorDTO.ErrorResponse;
import com.kodikas.backend.dto.userDTO.*;
import com.kodikas.backend.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retorna todos os usuários ativos.
     *
     * @return Lista de usuários ativos.
     */
    @GetMapping("/list")
    public ResponseEntity<List<ResponseListUsers>> getAllUsers() {
        List<ResponseListUsers> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Retorna os detalhes de um usuário pelo ID.
     *
     * @param id ID do usuário.
     * @return Detalhes do usuário.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDetailUserDTO> getUserById(@PathVariable Long id) {
        ResponseDetailUserDTO user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Cria um novo usuário.
     *
     * @param user Dados para criação do usuário.
     * @return Detalhes do usuário criado.
     */
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<ResponseCreateUserDTO> createUser(@Valid @RequestBody DataCreateUserDTO user) {
        ResponseCreateUserDTO createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id   ID do usuário.
     * @param user Dados atualizados do usuário.
     * @return Detalhes do usuário atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDetailUserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody DataUpdateUser user) {
        ResponseDetailUserDTO updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Realiza exclusão lógica de um usuário.
     *
     * @param id ID do usuário.
     * @return Status 204 (No Content) se for bem-sucedido.
     */
    @PutMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
