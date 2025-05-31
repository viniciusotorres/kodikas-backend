package com.kodikas.backend.service;

import com.kodikas.backend.dto.DataDetailsProject;
import com.kodikas.backend.dto.ResponseCreateProjectDTO;
import com.kodikas.backend.dto.ResponseListProject;
import com.kodikas.backend.model.Company;
import com.kodikas.backend.model.Project;
import com.kodikas.backend.model.User;
import com.kodikas.backend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    public List<ResponseListProject> getAllProjects() {
        return projectRepository.findAll().stream()
                .filter(project -> project.getAtivo() != null && project.getAtivo())
                .map(project -> new ResponseListProject(
                        project.getId(),
                        project.getName(),
                        project.getDescription(),
                        project.getAtivo(),
                        project.getUser().getId(),
                        project.getUser().getName(),
                        project.getCompany() != null ? project.getCompany().getId() : null,
                        project.getCompany() != null ? project.getCompany().getName() : null
                ))
                .toList();
    }

    public DataDetailsProject getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(project -> new DataDetailsProject(
                        project.getId(),
                        project.getName(),
                        project.getDescription(),
                        project.getAtivo(),
                        project.getUser().getId(),
                        project.getUser().getName(),
                        project.getCompany() != null ? project.getCompany().getId() : null,
                        project.getCompany() != null ? project.getCompany().getName() : null
                ))
                .orElse(null);
    }

    public ResponseCreateProjectDTO createProject(Project project) {
        User user = userService.getUserById(project.getUser().getId());
        Company company = companyService.getCompanyById(project.getCompany().getId());

        if (company == null) {
            throw new IllegalArgumentException("Empresa não encontrada");
        } else {
            project.setCompany(company);
        }

        if (user == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        } else {
            project.setUser(user);
        }

        return  new ResponseCreateProjectDTO(
                projectRepository.save(project).getId(),
                project.getName(),
                project.getDescription(),
                project.getAtivo(),
                user.getId(),
                company.getId()
        );
    }

    public DataDetailsProject updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id).orElse(null);
        Company company = companyService.getCompanyById(projectDetails.getCompany().getId());
        User user = userService.getUserById(projectDetails.getUser().getId());

        if (company == null) {
            throw new IllegalArgumentException("Empresa não encontrada");
        } else {
            projectDetails.setCompany(company);
        }

        if (user == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        } else {
            projectDetails.setUser(user);
        }
        if (project != null && project.getAtivo()) {
            project.setName(projectDetails.getName());
            project.setDescription(projectDetails.getDescription());
            project.setUser(user);
            project.setCompany(company);
            projectRepository.save(project);
            return new DataDetailsProject(
                    project.getId(),
                    project.getName(),
                    project.getDescription(),
                    project.getAtivo(),
                    user.getId(),
                    user.getName(),
                    company.getId(),
                    company.getName()
            );
        }
        return null;
    }

    public boolean deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null && project.getAtivo()) {
            project.setAtivo(false);
            projectRepository.save(project);
            return true;
        }
        return false;
    }

    public Project getProjectByIdForApplication(Long id) {
        return projectRepository.findById(id).orElse(null);
    }
}
