package com.kodikas.backend.dto.applicationsDTO;

import com.kodikas.backend.model.ApplicationStatus;

public record DataUpdateApplication(
    String name,
    String description,
    ApplicationStatus status,
    Long userId

) {
}
