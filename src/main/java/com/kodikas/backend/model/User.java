package com.kodikas.backend.model;

import com.kodikas.backend.dto.userDTO.DataUpdateUser;
import com.kodikas.backend.service.CompanyService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    @OneToMany(mappedBy = "user")
    private List<Application> applications;

    @Column(nullable = false)
    private Boolean ativo = true;

    public void updateFrom(DataUpdateUser dto, CompanyService companyService) {
        if (dto.name() != null) this.setName(dto.name());
        if (dto.email() != null) this.setEmail(dto.email());
        if (dto.companyId() != null) {
            Company company = companyService.getCompanyOrThrow(dto.companyId());
            this.setCompany(company);
        }
    }

}
