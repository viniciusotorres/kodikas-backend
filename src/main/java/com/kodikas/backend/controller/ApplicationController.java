package com.kodikas.backend.controller;

import com.kodikas.backend.constants.ApiPaths;
import com.kodikas.backend.dto.applicationsDTO.DataCreateApplicationDTO;
import com.kodikas.backend.dto.applicationsDTO.DataUpdateApplication;
import com.kodikas.backend.dto.applicationsDTO.ResponseCreateApplicationDTO;
import com.kodikas.backend.dto.applicationsDTO.ResponseDetailByIdDTO;
import com.kodikas.backend.service.ApplicationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas às aplicações.
 */
@RestController
@RequestMapping(ApiPaths.API_V1 + "/applcations")
public class ApplicationController {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private ApplicationService applicationService;

    /**
     * Obtém todas as aplicações ativas.
     *
     * @return Lista de aplicações ativas encapsulada em um ResponseEntity.
     */
    @GetMapping("/list")
    public ResponseEntity<List<ResponseCreateApplicationDTO>> getAllActiveApplications() {
        try {
            List<ResponseCreateApplicationDTO> applications = applicationService.getAllActiveApplications();
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            logger.error("Erro ao buscar aplicações ativas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtém os detalhes de uma aplicação pelo ID.
     *
     * @param id ID da aplicação.
     * @return Detalhes da aplicação encapsulados em um ResponseEntity.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDetailByIdDTO> getApplicationById(@PathVariable Long id) {
        try {
            ResponseDetailByIdDTO application = applicationService.getApplicationById(id);
            return ResponseEntity.ok(application);
        } catch (EntityNotFoundException e) {
            logger.error("Aplicação não encontrada com ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro inesperado ao buscar aplicação com ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cria uma nova aplicação.
     *
     * @param application Dados para criação da aplicação.
     * @return Detalhes da aplicação criada encapsulados em um ResponseEntity.
     */
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<ResponseCreateApplicationDTO> createApplication(@RequestBody @Valid DataCreateApplicationDTO application) {
        try {
            ResponseCreateApplicationDTO created = applicationService.createApplication(application);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (EntityNotFoundException e) {
            logger.error("Erro ao criar aplicação: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Erro inesperado ao criar aplicação: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Atualiza uma aplicação existente.
     *
     * @param id ID da aplicação a ser atualizada.
     * @param applicationDetails Dados para atualização da aplicação.
     * @return Detalhes da aplicação atualizada encapsulados em um ResponseEntity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDetailByIdDTO> updateApplication(@PathVariable Long id, @RequestBody DataUpdateApplication applicationDetails) {
        try {
            ResponseDetailByIdDTO updatedApplication = applicationService.updateApplication(id, applicationDetails);
            return ResponseEntity.ok(updatedApplication);
        } catch (EntityNotFoundException e) {
            logger.error("Erro ao atualizar aplicação: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar aplicação: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exclui uma aplicação pelo ID.
     *
     * @param id ID da aplicação a ser excluída.
     * @return ResponseEntity vazio indicando o sucesso ou falha da operação.
     */
    @PutMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        try {
            applicationService.deleteApplication(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.error("Erro ao excluir aplicação: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro inesperado ao excluir aplicação: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}