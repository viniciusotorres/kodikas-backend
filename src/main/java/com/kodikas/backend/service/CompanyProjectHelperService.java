package com.kodikas.backend.service;

import com.kodikas.backend.model.Company;
import com.kodikas.backend.model.Project;
import com.kodikas.backend.model.User;
import com.kodikas.backend.repository.ProjectRepository;
import com.kodikas.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Serviço auxiliar para gerenciar associações entre empresas, usuários e projetos.
 */
@Service
public class CompanyProjectHelperService {
    private static final Logger logger = LoggerFactory.getLogger(CompanyProjectHelperService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Obtém um usuário pelo ID.
     *
     * @param userId o ID do usuário a ser buscado
     * @return o usuário encontrado
     * @throws EntityNotFoundException se o usuário não for encontrado
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + userId));
    }

    /**
     * Obtém um projeto pelo ID.
     *
     * @param projectId o ID do projeto a ser buscado
     * @return o projeto encontrado
     * @throws EntityNotFoundException se o projeto não for encontrado
     */
    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado com o ID: " + projectId));
    }

    /**
     * Associa um usuário a uma empresa.
     *
     * @param user    o usuário a ser associado
     * @param company a empresa à qual o usuário será associado
     */
    public void associateUserWithCompany(User user, Company company) {
        user.setCompany(company);
        logger.info("Usuário com ID {} associado à empresa com ID {}", user.getId(), company.getId());
    }

    /**
     * Associa um projeto a uma empresa.
     *
     * @param project o projeto a ser associado
     * @param company a empresa à qual o projeto será associado
     */
    public void associateProjectWithCompany(Project project, Company company) {
        project.setCompany(company);
        logger.info("Projeto com ID {} associado à empresa com ID {}", project.getId(), company.getId());
    }
}