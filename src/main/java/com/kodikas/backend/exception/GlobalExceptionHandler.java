package com.kodikas.backend.exception;

import com.kodikas.backend.dto.errorDTO.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Classe responsável por capturar e tratar exceções lançadas pela aplicação
 * de forma centralizada, utilizando a anotação {@link RestControllerAdvice}.
 * Garante que os erros sejam convertidos em respostas padronizadas e amigáveis ao cliente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Trata exceções do tipo {@link EntityNotFoundException}, normalmente lançadas
     * quando uma entidade não é encontrada no banco de dados.
     *
     * @param ex Exceção capturada.
     * @return {@link ResponseEntity} com status 404 e corpo padronizado.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        logger.warn("Entidade não encontrada: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Recurso não encontrado",
                ex.getMessage(),
                String.valueOf(System.currentTimeMillis()),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Trata exceções do tipo {@link IllegalArgumentException}, geralmente relacionadas
     * a parâmetros inválidos em requisições.
     *
     * @param ex Exceção capturada.
     * @return {@link ResponseEntity} com status 400 e corpo padronizado.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("Requisição inválida: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Parâmetro inválido",
                ex.getMessage(),
                String.valueOf(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Trata exceções do tipo {@link MethodArgumentNotValidException}, que ocorrem
     * quando validações com anotações como {@code @Valid} falham.
     *
     * @param ex Exceção capturada.
     * @return {@link ResponseEntity} com status 400 e mensagens de erro detalhadas dos campos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        logger.warn("Erro de validação: {}", ex.getMessage());

        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse error = new ErrorResponse(
                "Erro de validação",
                details,
                String.valueOf(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Trata exceções do tipo {@link UserNotFoundException}, que são lançadas
     * quando um usuário não é encontrado no sistema.
     *
     * @param ex Exceção capturada contendo informações sobre o erro.
     * @return {@link ResponseEntity} com status 404 (Not Found) e um corpo padronizado
     * contendo detalhes do erro, como mensagem, timestamp e código de status HTTP.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        logger.warn("Usuário não encontrado: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Recurso não encontrado",
                ex.getMessage(),
                String.valueOf(System.currentTimeMillis()),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    /**
     * Captura qualquer outra exceção não tratada especificamente pelos outros handlers.
     * É uma proteção global contra erros inesperados.
     *
     * @param ex Exceção genérica capturada.
     * @return {@link ResponseEntity} com status 500 e mensagem genérica.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Erro interno do servidor: {}", ex.getMessage(), ex);

        ErrorResponse error = new ErrorResponse(
                "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                String.valueOf(System.currentTimeMillis()),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
