package com.kodikas.backend.controller;

import com.kodikas.backend.constants.ApiPaths;
import com.kodikas.backend.model.Application;
import com.kodikas.backend.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.API_V1 + "/applcations")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/list")
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(Long id) {
        Application application = applicationService.getApplicationById(id);
        return (application != null)
                ? ResponseEntity.ok(application)
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Application> createApplication(Application application) {
        Application createdApplication = applicationService.createApplication(application);
        return ResponseEntity.status(201).body(createdApplication);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody Application applicationDetails) {
        Application updatedApplication = applicationService.updateApplication(id, applicationDetails);
        return (updatedApplication != null)
                ? ResponseEntity.ok(updatedApplication)
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        boolean isDeleted = applicationService.deleteApplication(id);
        return isDeleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }


}
