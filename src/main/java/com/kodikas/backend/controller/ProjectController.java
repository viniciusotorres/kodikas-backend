package com.kodikas.backend.controller;

import com.kodikas.backend.constants.ApiPaths;
import com.kodikas.backend.dto.projectsDTO.*;
import com.kodikas.backend.service.ProjectService;
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
 * Controlador responsável por gerenciar as operações relacionadas aos projetos.
 */
@RestController
@RequestMapping(ApiPaths.API_V1 + "/projects")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    /**
     * Retorna todos os projetos ativos.
     *
     * @return ResponseEntity contendo a lista de projetos ativos.
     */
    @GetMapping("/list")
    public ResponseEntity<List<ResponseListProject>> getAllProjects() {
        try {
            List<ResponseListProject> projects = projectService.getAllProjects();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            logger.error("Erro ao buscar projetos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retorna os detalhes de um projeto pelo ID.
     *
     * @param id ID do projeto.
     * @return ResponseEntity contendo os detalhes do projeto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDetailsProject> getProjectById(@PathVariable Long id) {
        try {
            ResponseDetailsProject project = projectService.getProjectById(id);
            return ResponseEntity.ok(project);
        } catch (IllegalArgumentException e) {
            logger.error("Projeto não encontrado com ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Erro ao buscar projeto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cria um novo projeto.
     *
     * @param project Dados para criação do projeto.
     * @return ResponseEntity contendo os detalhes do projeto criado.
     */
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<ResponseCreateProjectDTO> createProject(@RequestBody @Valid DataCreateProjectDTO project) {
        try {
            ResponseCreateProjectDTO createdProject = projectService.createProject(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } catch (EntityNotFoundException e) {
            logger.error("Erro ao criar projeto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Erro ao criar projeto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Atualiza os dados de um projeto existente.
     *
     * @param id ID do projeto a ser atualizado.
     * @param projectDetails Dados para atualização do projeto.
     * @return ResponseEntity contendo os detalhes do projeto atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDetailsProject> updateProject(@PathVariable Long id, @RequestBody DataUpdateProject projectDetails) {
        try {
            ResponseDetailsProject updatedProject = projectService.updateProject(id, projectDetails);
            return ResponseEntity.ok(updatedProject);
        } catch (EntityNotFoundException e) {
            logger.error("Projeto não encontrado com ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar projeto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exclui logicamente um projeto pelo ID.
     *
     * @param id ID do projeto a ser excluído.
     * @return ResponseEntity vazio indicando o sucesso ou falha da operação.
     */
    @PutMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.error("Projeto não encontrado com ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Erro ao deletar projeto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}