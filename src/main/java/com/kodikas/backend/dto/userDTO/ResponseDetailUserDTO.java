package com.kodikas.backend.dto.userDTO;

public record ResponseDetailUserDTO(
        Long id,
        String name,
        String email,
        Boolean ativo,
        Long companyId,
        String createdAt
) {
}
