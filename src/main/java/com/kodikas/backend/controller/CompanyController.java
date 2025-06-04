package com.kodikas.backend.controller;

import com.kodikas.backend.constants.ApiPaths;
import com.kodikas.backend.dto.companiesDTO.DataCreateCompanyDTO;
import com.kodikas.backend.dto.companiesDTO.DataUpdateCompany;
import com.kodikas.backend.dto.companiesDTO.ResponseCreateCompany;
import com.kodikas.backend.service.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas às empresas.
 */
@RestController
@RequestMapping(ApiPaths.API_V1 + "/companies")
public class CompanyController {
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private CompanyService companyService;

    /**
     * Retorna todas as empresas ativas.
     *
     * @return ResponseEntity contendo a lista de empresas ativas.
     */
    @GetMapping("/list")
    public ResponseEntity<List<ResponseCreateCompany>> getAllCompanies() {
        try {
            List<ResponseCreateCompany> companies = companyService.getAllActiveCompanies();
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            logger.error("Erro ao buscar empresas ativas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retorna os detalhes de uma empresa pelo ID.
     *
     * @param id ID da empresa.
     * @return ResponseEntity contendo os detalhes da empresa.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseCreateCompany> getCompanyById(@PathVariable Long id) {
        try {
            ResponseCreateCompany company = companyService.getCompanyById(id);
            return ResponseEntity.ok(company);
        } catch (EntityNotFoundException e) {
            logger.error("Empresa não encontrada com ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao buscar empresa com ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cria uma nova empresa.
     *
     * @param company Dados para criação da empresa.
     * @return ResponseEntity contendo os detalhes da empresa criada.
     */
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<ResponseCreateCompany> createCompany(@RequestBody @Valid DataCreateCompanyDTO company) {
        try {
            ResponseCreateCompany createdCompany = companyService.createCompany(company);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
        } catch (EntityNotFoundException e) {
            logger.error("Erro ao criar empresa: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Erro ao criar empresa: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Atualiza os dados de uma empresa existente.
     *
     * @param id      ID da empresa a ser atualizada.
     * @param company Dados para atualização da empresa.
     * @return ResponseEntity contendo os detalhes da empresa atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseCreateCompany> updateCompany(@PathVariable Long id, @RequestBody DataUpdateCompany company) {
        try {
            ResponseCreateCompany updatedCompany = companyService.updateCompany(id, company);
            return ResponseEntity.ok(updatedCompany);
        } catch (EntityNotFoundException e) {
            logger.error("Empresa não encontrada com ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar empresa com ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exclui logicamente uma empresa pelo ID.
     *
     * @param id ID da empresa a ser excluída.
     * @return ResponseEntity vazio indicando o sucesso ou falha da operação.
     */
    @PutMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.error("Erro ao excluir Empresa. Empresa não encontrada com ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro inesperado ao excluir empresa com ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}