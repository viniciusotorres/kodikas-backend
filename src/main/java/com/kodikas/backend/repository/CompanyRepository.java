package com.kodikas.backend.repository;

import com.kodikas.backend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByAtivoTrue();
}