package com.kodikas.backend.repository;

import com.kodikas.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
List<User> findByAtivoTrue();

}
