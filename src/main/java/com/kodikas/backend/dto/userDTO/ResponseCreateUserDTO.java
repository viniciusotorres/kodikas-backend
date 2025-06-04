package com.kodikas.backend.dto.userDTO;

public record ResponseCreateUserDTO(
        Long id,
        String name,
        String email,
        Boolean ativo,
        Long companyId,
        String createdAt
) {
}
