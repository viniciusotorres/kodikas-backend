package com.kodikas.backend.dto.companiesDTO;

public record ResponseDetailByIdDTO(
        Long id,
        String name,
        String description,
        String createdAt,
        Boolean ativo
) {
}
