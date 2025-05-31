package com.kodikas.backend.dto.applicationsDTO;

public record ResponseDetailByIdDTO(
        Long id,
        String name,
        String description,
        String status,
        Long userId,
        String userName
) {
}
