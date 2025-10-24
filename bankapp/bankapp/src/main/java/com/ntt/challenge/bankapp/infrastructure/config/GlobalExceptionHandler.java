package com.ntt.challenge.bankapp.infrastructure.config;

import com.ntt.challenge.bankapp.domain.exception.InsufficientBalanceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;

@Slf4j
@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
    public ResponseEntity<Map<String, String>> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        log.error("Insufficient balance: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
    public Map<String, String> handleDataIntegrity(DataIntegrityViolationException ex) {
        String mensaje = "La identificación u otro dato único ya existe en la base de datos.";

        log.warn("Conflicto de integridad de datos: {}", mensaje);

        return Map.of(
                "error", "Conflicto de Datos",
                "mensaje", mensaje);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
    public Map<String, String> handleGenericException(RuntimeException ex) {
        log.error("Error inesperado en la aplicación: {}", ex.getMessage(), ex);
        return Map.of("error", "Error Interno", "mensaje", "Ocurrió un error inesperado, contacte al admin.");
    }
}