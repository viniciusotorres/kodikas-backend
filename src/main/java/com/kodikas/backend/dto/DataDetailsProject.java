package com.kodikas.backend.dto;

public record DataDetailsProject(
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
