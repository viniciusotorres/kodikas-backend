package com.kodikas.backend.repository;

import com.kodikas.backend.dto.applicationsDTO.ResponseCreateApplicationDTO;
import com.kodikas.backend.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepositoy extends JpaRepository<Application, Long> {
    List<ResponseCreateApplicationDTO> findByAtivoTrue();
}
