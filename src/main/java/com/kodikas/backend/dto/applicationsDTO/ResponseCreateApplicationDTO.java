package com.kodikas.backend.dto.applicationsDTO;

import com.kodikas.backend.model.ApplicationStatus;

public record ResponseCreateApplicationDTO(
        Long id,
        String name,
        String description,
        ApplicationStatus status,
        Long userId,
        String userName
) {
}
