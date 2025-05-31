package com.kodikas.backend.controller;

import com.kodikas.backend.constants.ApiPaths;
import com.kodikas.backend.dto.DataDetailsProject;
import com.kodikas.backend.dto.ResponseCreateProjectDTO;
import com.kodikas.backend.dto.ResponseListProject;
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
    public ResponseEntity<DataDetailsProject> getProjectById(@PathVariable Long id) {
        DataDetailsProject project = projectService.getProjectById(id);
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
    public ResponseEntity<DataDetailsProject> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        DataDetailsProject updatedProject = projectService.updateProject(id, projectDetails);
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
