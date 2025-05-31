package com.kodikas.backend.dto.applicationsDTO;

import com.kodikas.backend.model.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DataCreateApplicationDTO(
        @NotBlank(message = "O nome não pode estar vazio.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String name,

        @NotBlank(message = "A descrição não pode estar vazia.")
        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
        String description,

        @NotNull(message = "O status não pode ser nulo.")
        ApplicationStatus status,

        @NotNull(message = "O ID do usuário não pode ser nulo.")
        Long userId
) {
}