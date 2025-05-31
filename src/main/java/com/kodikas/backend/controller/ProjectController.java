package com.kodikas.backend.controller;

import com.kodikas.backend.constants.ApiPaths;
import com.kodikas.backend.dto.projectsDTO.ResponseDetailsProject;
import com.kodikas.backend.dto.projectsDTO.ResponseCreateProjectDTO;
import com.kodikas.backend.dto.projectsDTO.ResponseListProject;
import com.kodikas.backend.model.Project;
import com.kodikas.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.API_V1 + "/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/list")
    public List<ResponseListProject> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDetailsProject> getProjectById(@PathVariable Long id) {
        ResponseDetailsProject project = projectService.getProjectById(id);
        return (project != null)
                ? ResponseEntity.ok(project)
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseCreateProjectDTO> createProject(@RequestBody Project project) {
        ResponseCreateProjectDTO createdProject = projectService.createProject(project);
        return ResponseEntity.status(201).body(createdProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDetailsProject> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        ResponseDetailsProject updatedProject = projectService.updateProject(id, projectDetails);
        return (updatedProject != null)
                ? ResponseEntity.ok(updatedProject)
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        boolean isDeleted = projectService.deleteProject(id);
        return isDeleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
