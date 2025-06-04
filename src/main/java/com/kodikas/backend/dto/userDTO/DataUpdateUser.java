package com.kodikas.backend.dto.userDTO;

public record DataUpdateUser(
        String name,
        String email,
        Boolean ativo,
        Long companyId
) {
}
