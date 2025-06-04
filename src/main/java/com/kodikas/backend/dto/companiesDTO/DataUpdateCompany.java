package com.kodikas.backend.dto.companiesDTO;

import java.util.List;

public record DataUpdateCompany(
        String name,
        String description,
        Boolean ativo,
        List<Long> usersIds,
        List<Long> projectsIds
) {
}
