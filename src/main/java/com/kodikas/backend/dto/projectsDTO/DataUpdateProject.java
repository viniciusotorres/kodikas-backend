package com.kodikas.backend.dto.projectsDTO;

public record DataUpdateProject(
        String name,
        String description,
        Long userId,
        Long companyId
) {
}
