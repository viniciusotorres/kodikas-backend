package com.kodikas.backend.dto;

public record ResponseCreateProjectDTO(
        Long id,
        String name,
        String description,
        Boolean ativo,
        Long userId,
        Long companyId
) {
}
