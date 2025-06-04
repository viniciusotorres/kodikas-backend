package com.kodikas.backend.dto.companiesDTO;

import java.util.List;

public record ResponseCreateCompany(
        Long id,
        String name,
        String description,
        String createdAt,
        Boolean ativo,
        List<Long> usersIds,
        List<Long> projectsIds


) {
}
