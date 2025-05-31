package com.kodikas.backend.dto.projectsDTO;

public record ResponseDetailsProject(
        Long id,
        String name,
        String description,
        Boolean ativo,
        Long userId,
        String userName,
        Long companyId,
        String companyName
) {
}
