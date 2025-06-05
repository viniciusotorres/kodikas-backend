package com.kodikas.backend.dto.errorDTO;

public record ErrorResponse(
        String message,
        String details,
        String timestamp,
        int statusCode
) {
}
