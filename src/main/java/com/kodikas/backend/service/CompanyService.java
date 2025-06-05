package com.kodikas.backend.service;

import com.kodikas.backend.dto.companiesDTO.DataCreateCompanyDTO;
import com.kodikas.backend.dto.companiesDTO.DataUpdateCompany;
import com.kodikas.backend.dto.companiesDTO.ResponseCreateCompany;
import com.kodikas.backend.exception.CompanyNotFoundException;
import com.kodikas.backend.model.Company;
import com.kodikas.backend.model.Project;
import com.kodikas.backend.model.User;
import com.kodikas.backend.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável por gerenciar as operações relacionadas às empresas.
 */
@Service
public class CompanyService {
    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyProjectHelperService helperService;

    /**
     * Obtém todas as empresas ativas.
     *
     * @return Lista de empresas ativas.
     */
    public List<ResponseCreateCompany> getAllActiveCompanies() {
        return companyRepository.findAll().stream()
                .filter(Company::getAtivo)
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Obtém os detalhes de uma empresa pelo ID.
     *
     * @param id ID da empresa.
     * @return Detalhes da empresa.
     * @throws EntityNotFoundException se a empresa não for encontrada.
     */
    public ResponseCreateCompany getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com o ID: " + id));

        logger.info("Empresa encontrada com ID: {}", id);
        return mapToResponse(company);
    }

    /**
     * Cria uma nova empresa.
     *
     * @param company Dados para criação da empresa.
     * @return Detalhes da empresa criada.
     */
    public ResponseCreateCompany createCompany(DataCreateCompanyDTO company) {
        Company newCompany = new Company();
        newCompany.setName(company.name());
        newCompany.setDescription(company.description());
        newCompany.setCreatedAt(LocalDateTime.now());
        newCompany.setAtivo(true);

        Company savedCompany = companyRepository.save(newCompany);
        logger.info("Empresa criada com ID: {}", savedCompany.getId());
        return mapToResponse(savedCompany);
    }

    /**
     * Atualiza os dados de uma empresa existente.
     *
     * @param id             ID da empresa a ser atualizada.
     * @param companyDetails Dados para atualização da empresa.
     * @return Detalhes da empresa atualizada.
     * @throws EntityNotFoundException se a empresa não for encontrada.
     */
    public ResponseCreateCompany updateCompany(Long id, DataUpdateCompany companyDetails) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com o ID: " + id));

        logger.info("Atualizando empresa com ID: {}", id);

        if (companyDetails.name() != null) {
            company.setName(companyDetails.name());
        }
        if (companyDetails.description() != null) {
            company.setDescription(companyDetails.description());
        }

        updateUsersForCompany(company, companyDetails.usersIds());
        updateProjectsForCompany(company, companyDetails.projectsIds());

        Company updatedCompany = companyRepository.save(company);
        logger.info("Empresa atualizada com ID: {}", updatedCompany.getId());
        return mapToResponse(updatedCompany);
    }

    /**
     * Exclui logicamente uma empresa pelo ID.
     *
     * @param id ID da empresa a ser excluída.
     * @throws EntityNotFoundException se a empresa não for encontrada ou se houver usuários ou projetos associados.
     */
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com o ID: " + id));

        if (!company.getAtivo()) {
            logger.warn("Tentativa de excluir logicamente uma empresa já inativa com ID: {}", id);
            throw new EntityNotFoundException("Empresa já está inativa com o ID: " + id);
        }

        if (company.getUsers() != null && !company.getUsers().isEmpty()) {
            logger.warn("Tentativa de excluir logicamente uma empresa com usuários associados. ID: {}", id);
            throw new EntityNotFoundException("Não é possível excluir logicamente uma empresa com usuários associados. ID: " + id);
        }

        if (company.getProjects() != null && !company.getProjects().isEmpty()) {
            logger.warn("Tentativa de excluir logicamente uma empresa com projetos associados. ID: {}", id);
            throw new EntityNotFoundException("Não é possível excluir logicamente uma empresa com projetos associados. ID: " + id);
        }

        logger.info("Excluindo logicamente a empresa com ID: {}", id);
        company.setAtivo(false);
        companyRepository.save(company);
        logger.info("Empresa excluída logicamente com sucesso. ID: {}", id);
    }

    /**
     * Obtém uma empresa pelo ID.
     *
     * @param id ID da empresa.
     * @return Entidade da empresa.
     * @throws EntityNotFoundException se a empresa não for encontrada.
     */
    public Company getCompany(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com o ID: " + id));
    }

    /**
     * Mapeia uma entidade Company para um DTO de resposta.
     *
     * @param company Entidade da empresa.
     * @return DTO de resposta da empresa.
     */
    private ResponseCreateCompany mapToResponse(Company company) {
        return new ResponseCreateCompany(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getCreatedAt().toString(),
                company.getAtivo(),
                company.getUsers() != null ? company.getUsersIds() : List.of(),
                company.getProjects() != null ? company.getProjectsIds() : List.of()
        );
    }

    /**
     * Atualiza os usuários associados a uma empresa.
     *
     * @param company  Empresa a ser atualizada.
     * @param usersIds IDs dos usuários a serem associados.
     */
    private void updateUsersForCompany(Company company, List<Long> usersIds) {
        if (usersIds != null && !usersIds.isEmpty()) {
            if (company.getUsers() == null) {
                company.setUsers(new ArrayList<>());
            }

            for (Long userId : usersIds) {
                boolean userAlreadyExists = company.getUsers().stream()
                        .anyMatch(user -> user.getId().equals(userId));
                if (!userAlreadyExists) {
                    User user = helperService.getUserById(userId);
                    helperService.associateUserWithCompany(user, company);
                    company.getUsers().add(user);
                }
            }
        }
    }

    /**
     * Atualiza os projetos associados a uma empresa.
     *
     * @param company     Empresa a ser atualizada.
     * @param projectsIds IDs dos projetos a serem associados.
     */
    private void updateProjectsForCompany(Company company, List<Long> projectsIds) {
        if (projectsIds != null && !projectsIds.isEmpty()) {
            if (company.getProjects() == null) {
                company.setProjects(new ArrayList<>());
            }

            for (Long projectId : projectsIds) {
                boolean projectAlreadyExists = company.getProjects().stream()
                        .anyMatch(project -> project.getId().equals(projectId));
                if (!projectAlreadyExists) {
                    Project project = helperService.getProjectById(projectId);
                    helperService.associateProjectWithCompany(project, company);
                    company.getProjects().add(project);
                }
            }
        }
    }

    public Company getCompanyOrThrow(Long id) {
        Company company = getCompany(id);
        if (company == null) {
            throw new CompanyNotFoundException(id);
        }
        return company;
    }
}