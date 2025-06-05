package com.kodikas.backend.service;

import com.kodikas.backend.dto.projectsDTO.*;
import com.kodikas.backend.model.Company;
import com.kodikas.backend.model.Project;
import com.kodikas.backend.model.User;
import com.kodikas.backend.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável por gerenciar as operações relacionadas aos projetos.
 */
@Service
public class ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    /**
     * Obtém todos os projetos ativos.
     *
     * @return Lista de DTOs de projetos ativos.
     */
    public List<ResponseListProject> getAllProjects() {
        return projectRepository.findAll().stream()
                .filter(project -> project.getAtivo() != null && project.getAtivo())
                .map(this::mapToResponseList)
                .toList();
    }

    /**
     * Obtém os detalhes de um projeto pelo ID.
     *
     * @param id ID do projeto.
     * @return DTO com os detalhes do projeto.
     * @throws IllegalArgumentException se o projeto não for encontrado.
     */
    public ResponseDetailsProject getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado com o ID: " + id));

        logger.info("Projeto encontrado com ID: {}", id);
        return mapToResponseDetails(project);
    }

    /**
     * Cria um novo projeto.
     *
     * @param project Dados para criação do projeto.
     * @return DTO com os detalhes do projeto criado.
     * @throws IllegalArgumentException se o usuário ou a empresa não forem encontrados.
     */
    public ResponseCreateProjectDTO createProject(DataCreateProjectDTO project) {
        User user = userService.getUserById(project.userId());
        Company company = companyService.getCompany(project.companyId());

        if (company == null) {
            logger.error("Empresa não encontrada com ID: {}", project.companyId());
            throw new IllegalArgumentException("Empresa não encontrada");
        }

        if (user == null) {
            logger.error("Usuário não encontrado com ID: {}", project.userId());
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        Project newProject = new Project();
        newProject.setName(project.name());
        newProject.setDescription(project.description());
        newProject.setAtivo(true);
        newProject.setUser(user);
        newProject.setCreatedAt(LocalDateTime.now());
        newProject.setCompany(company);

        logger.info("Cadastrando novo projeto: {}", newProject.getName());

        Project savedProject = projectRepository.save(newProject);

        return new ResponseCreateProjectDTO(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getDescription(),
                savedProject.getAtivo(),
                user.getId(),
                company.getId()
        );
    }

    /**
     * Atualiza os dados de um projeto existente.
     *
     * @param id ID do projeto a ser atualizado.
     * @param projectDetails Dados para atualização do projeto.
     * @return DTO com os detalhes do projeto atualizado.
     * @throws IllegalArgumentException se o usuário ou a empresa não forem encontrados.
     */
    public ResponseDetailsProject updateProject(Long id, DataUpdateProject projectDetails) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Projeto não encontrado com o ID: " + id));

        if (!project.getAtivo()) {
            return null;
        }

        User user = projectDetails.userId() != null ? userService.getUserById(projectDetails.userId()) : null;
        if (user == null && projectDetails.userId() != null) {
            logger.error("Usuário não encontrado com ID: {}", projectDetails.userId());
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        Company company = projectDetails.companyId() != null ? companyService.getCompany(projectDetails.companyId()) : null;
        if (company == null && projectDetails.companyId() != null) {
            logger.error("Empresa não encontrada com ID: {}", projectDetails.companyId());
            throw new IllegalArgumentException("Empresa não encontrada");
        }

        project.setName(projectDetails.name() != null ? projectDetails.name() : project.getName());
        project.setDescription(projectDetails.description() != null ? projectDetails.description() : project.getDescription());
        if (user != null) project.setUser(user);
        if (company != null) project.setCompany(company);

        projectRepository.save(project);

        return mapToResponseDetails(project);
    }

    /**
     * Exclui logicamente um projeto pelo ID.
     *
     * @param id ID do projeto a ser excluído.
     * @throws EntityNotFoundException se o projeto não for encontrado.
     */
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado com o ID: " + id));

        logger.info("Excluindo logicamente projeto com ID: {}", id);
        project.setAtivo(false);
        projectRepository.save(project);
        logger.info("Projeto excluído logicamente com sucesso. ID: {}", id);
    }

    /**
     * Obtém um projeto pelo ID.
     *
     * @param id ID do projeto.
     * @return Entidade Project ou null se não encontrado.
     */
    public Project getProject(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    /**
     * Mapeia uma entidade Project para um DTO de lista.
     *
     * @param project Entidade Project.
     * @return DTO de lista de projetos.
     */
    private ResponseListProject mapToResponseList(Project project) {
        return new ResponseListProject(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getAtivo(),
                project.getUser().getId(),
                project.getUser().getName(),
                project.getCompany() != null ? project.getCompany().getId() : null,
                project.getCompany() != null ? project.getCompany().getName() : null
        );
    }

    /**
     * Mapeia uma entidade Project para um DTO detalhado.
     *
     * @param project Entidade Project.
     * @return DTO detalhado do projeto.
     */
    private ResponseDetailsProject mapToResponseDetails(Project project) {
        return new ResponseDetailsProject(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getAtivo(),
                project.getUser().getId(),
                project.getUser().getName(),
                project.getCompany() != null ? project.getCompany().getId() : null,
                project.getCompany() != null ? project.getCompany().getName() : null
        );
    }
}