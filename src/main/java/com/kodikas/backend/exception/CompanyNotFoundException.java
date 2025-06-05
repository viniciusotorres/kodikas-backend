package com.kodikas.backend.exception;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(Long id) {
        super("Empresa não encontrada com o ID: " + id);
    }
}
