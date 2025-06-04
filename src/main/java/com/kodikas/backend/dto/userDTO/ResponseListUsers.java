package com.kodikas.backend.dto.userDTO;

public record ResponseListUsers(
        Long id,
        String name,
        String email,
        Boolean ativo,
        Long companyId
) {
}
