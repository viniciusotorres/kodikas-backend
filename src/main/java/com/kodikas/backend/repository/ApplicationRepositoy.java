package com.kodikas.backend.repository;

import com.kodikas.backend.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepositoy extends JpaRepository<Application, Long> {
}
