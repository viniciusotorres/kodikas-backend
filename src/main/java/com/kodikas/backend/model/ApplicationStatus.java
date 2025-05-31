package com.kodikas.backend.model;

public enum ApplicationStatus {
    PENDENTE("Pendente"),
    APROVADO("Aprovado"),
    REJEITADO("Rejeitado"),
    CANCELADO("Cancelado"),
    EM_PROCESSO("Em Processo");

    private final String status;

    ApplicationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}