package com.kodikas.backend.service;

import com.kodikas.backend.dto.DataDetailsProject;
import com.kodikas.backend.model.Application;
import com.kodikas.backend.model.Company;
import com.kodikas.backend.model.Project;
import com.kodikas.backend.model.User;
import com.kodikas.backend.repository.ApplicationRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepositoy applicationRepositoy;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CompanyService companyService;

    public List<Application> getAllApplications() {
        return applicationRepositoy.findAll();
    }

    public Application getApplicationById(Long id) {
        return applicationRepositoy.findById(id).orElse(null);
    }

    public Application createApplication(Application application) {
        User user = userService.getUserById(application.getUser().getId());
        Company company = companyService.getCompanyById(application.getCompany().getId());
        Project project = projectService.getProjectByIdForApplication(application.getProject().getId());

        if (project == null) {
            throw new IllegalArgumentException("Projeto não encontrado");
        } else {
            application.setProject(project);
        }

        if (company == null) {
            throw new IllegalArgumentException("Empresa não encontrada");
        } else {
            application.setCompany(company);
        }

        if (user == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        } else {
            application.setUser(user);
        }


        return applicationRepositoy.save(application);
    }

    public Application updateApplication(Long id, Application applicationDetails) {
        Application application = applicationRepositoy.findById(id).orElse(null);
        if (application != null) {
            application.setStatus(applicationDetails.getStatus());
            application.setAppliedAt(applicationDetails.getAppliedAt());
            application.setUser(applicationDetails.getUser());
            application.setProject(applicationDetails.getProject());
            application.setCompany(applicationDetails.getCompany());

            return applicationRepositoy.save(application);
        }
        return null;
    }


    public boolean deleteApplication(Long id) {
        Application application = applicationRepositoy.findById(id).orElse(null);
        if (application != null) {
            applicationRepositoy.delete(application);
            return true;
        }
        return false;
    }
}
