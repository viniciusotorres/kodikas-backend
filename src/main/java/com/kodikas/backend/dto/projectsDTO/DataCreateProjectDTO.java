package com.kodikas.backend.dto.projectsDTO;

import com.kodikas.backend.model.Company;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DataCreateProjectDTO(
        @NotBlank(message = "O nome do projeto não pode estar vazio.")
        String name,

        @NotBlank(message = "A descrição do projeto não pode estar vazia.")
        String description,

        @NotNull(message = "O ID do usuário não pode estar vazio.")
        Long userId,

        @NotNull(message = "O ID da empresa não pode estar vazio.")
        Long companyId
) {


}
