package com.kodikas.backend.dto.userDTO;

import jakarta.validation.constraints.NotBlank;

public record DataCreateUserDTO(
        @NotBlank(message = "O nome não pode estar vazio.")
        String name,
        @NotBlank(message = "O email não pode estar vazio.")
        String email,
        @NotBlank(message = "A senha não pode estar vazia.")
        String password
) {
}
