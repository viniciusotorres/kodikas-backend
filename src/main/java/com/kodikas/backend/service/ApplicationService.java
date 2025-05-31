package com.kodikas.backend.service;

import com.kodikas.backend.dto.applicationsDTO.DataCreateApplicationDTO;
import com.kodikas.backend.dto.applicationsDTO.DataUpdateApplication;
import com.kodikas.backend.dto.applicationsDTO.ResponseCreateApplicationDTO;
import com.kodikas.backend.dto.applicationsDTO.ResponseDetailByIdDTO;
import com.kodikas.backend.model.*;
import com.kodikas.backend.repository.ApplicationRepositoy;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;

/**
 * Serviço responsável por gerenciar as operações relacionadas às aplicações.
 */
@Service
public class ApplicationService {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    @Autowired
    private ApplicationRepositoy applicationRepository;

    @Autowired
    private UserService userService;

    /**
     * Obtém todas as aplicações ativas.
     *
     * @return Lista de DTOs de aplicações ativas.
     */
    public List<ResponseCreateApplicationDTO> getAllActiveApplications() {
        return applicationRepository.findAll().stream()
                .filter(application -> application.getAtivo())
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Obtém os detalhes de uma aplicação pelo ID.
     *
     * @param id ID da aplicação.
     * @return DTO com os detalhes da aplicação.
     * @throws EntityNotFoundException se a aplicação não for encontrada.
     */
    public ResponseDetailByIdDTO getApplicationById(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aplicação não encontrada com o ID: " + id));

        logger.info("Aplicação encontrada com ID: {}", id);
        return mapToDetailResponse(application);
    }

    /**
     * Cria uma nova aplicação.
     *
     * @param dto Dados para criação da aplicação.
     * @return DTO com os detalhes da aplicação criada.
     * @throws EntityNotFoundException se o usuário associado não for encontrado.
     */
    public ResponseCreateApplicationDTO createApplication(DataCreateApplicationDTO dto) {
        Application newApplication = mapToEntityCreate(dto);
        Application savedApplication = applicationRepository.save(newApplication);

        logger.info("Aplicação cadastrada com ID: {}", savedApplication.getId());
        return mapToResponse(savedApplication);
    }

    /**
     * Atualiza uma aplicação existente.
     *
     * @param id ID da aplicação a ser atualizada.
     * @param applicationDetails Dados para atualização da aplicação.
     * @return DTO com os detalhes da aplicação atualizada.
     * @throws EntityNotFoundException se a aplicação ou o usuário associado não forem encontrados.
     */
    public ResponseDetailByIdDTO updateApplication(Long id, DataUpdateApplication applicationDetails) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aplicação não encontrada com o ID: " + id));

        logger.info("Atualizando aplicação com ID: {}", id);

        application.setName(applicationDetails.name());
        application.setDescription(applicationDetails.description());
        application.setStatus(applicationDetails.status());

        User user = userService.getUserById(applicationDetails.userId());
        if (user == null) {
            throw new EntityNotFoundException("Usuário não encontrado com o ID: " + applicationDetails.userId());
        }
        application.setUser(user);

        Application updatedApplication = applicationRepository.save(application);
        logger.info("Aplicação atualizada com sucesso. ID: {}", updatedApplication.getId());

        return mapToDetailResponse(updatedApplication);
    }

    /**
     * Exclui logicamente uma aplicação pelo ID.
     *
     * @param id ID da aplicação a ser excluída.
     * @throws EntityNotFoundException se a aplicação não for encontrada.
     */
    public void deleteApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aplicação não encontrada com o ID: " + id));

        logger.info("Excluindo logicamente aplicação com ID: {}", id);
        application.setAtivo(false);
        applicationRepository.save(application);
        logger.info("Aplicação excluída logicamente com sucesso. ID: {}", id);
    }

    /**
     * Mapeia os dados de criação para uma entidade Application.
     *
     * @param dto Dados de criação da aplicação.
     * @return Entidade Application.
     * @throws EntityNotFoundException se o usuário associado não for encontrado.
     */
    private Application mapToEntityCreate(DataCreateApplicationDTO dto) {
        Application app = new Application();
        app.setName(dto.name());
        app.setDescription(dto.description());
        app.setStatus(dto.status());
        app.setAppliedAt(LocalDateTime.now());

        User user = userService.getUserById(dto.userId());
        if (user == null) {
            throw new EntityNotFoundException("Usuário não encontrado com o ID: " + dto.userId());
        }
        app.setUser(user);
        return app;
    }

    /**
     * Mapeia uma entidade Application para um DTO de criação.
     *
     * @param application Entidade Application.
     * @return DTO de criação da aplicação.
     */
    private ResponseCreateApplicationDTO mapToResponse(Application application) {
        return new ResponseCreateApplicationDTO(
                application.getId(),
                application.getName(),
                application.getDescription(),
                application.getStatus(),
                application.getUser().getId(),
                application.getUser().getName()
        );
    }

    /**
     * Mapeia uma entidade Application para um DTO detalhado.
     *
     * @param application Entidade Application.
     * @return DTO detalhado da aplicação.
     */
    private ResponseDetailByIdDTO mapToDetailResponse(Application application) {
        return new ResponseDetailByIdDTO(
                application.getId(),
                application.getName(),
                application.getDescription(),
                application.getStatus().name(),
                application.getUser().getId(),
                application.getUser().getName()
        );
    }
}