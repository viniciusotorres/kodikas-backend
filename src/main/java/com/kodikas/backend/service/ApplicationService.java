package com.kodikas.backend.service;

import com.kodikas.backend.model.Application;
import com.kodikas.backend.repository.ApplicationRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepositoy applicationRepositoy;

    public List<Application> getAllApplications() {
        return applicationRepositoy.findAll();
    }

    public Application getApplicationById(Long id) {
        return applicationRepositoy.findById(id).orElse(null);
    }

    public Application createApplication(Application application) {
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


    public void deleteApplication(Long id) {
        applicationRepositoy.deleteById(id);
    }
}
