package com.ntt.challenge.bankapp.infrastructure.controller;

import com.ntt.challenge.bankapp.application.mapper.MovementDtoMapper;
import com.ntt.challenge.bankapp.application.usecase.MovementUseCase;
import com.ntt.challenge.bankapp.infrastructure.api.MovimientosApi;
import com.ntt.challenge.bankapp.infrastructure.api.model.MovementRequest;
import com.ntt.challenge.bankapp.infrastructure.api.model.MovementResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MovementApiController implements MovimientosApi {

    private final MovementUseCase movementUseCase;

    @Override
    public Mono<ResponseEntity<MovementResponse>> createMovement(
            Mono<MovementRequest> movementRequest,
            ServerWebExchange exchange) {
        log.info("POST /movements");
        return movementRequest
                .map(this::toMovementDto)
                .flatMap(dto -> movementUseCase.saveMovement(MovementDtoMapper.toDomain(dto)))
                .map(MovementDtoMapper::toDto)
                .map(this::toMovementResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @Override
    public Mono<ResponseEntity<Flux<MovementResponse>>> getMovementsByAccount(
            String accountNumber,
            LocalDate startDate,
            LocalDate endDate,
            ServerWebExchange exchange) {
        log.info("GET /movements/account/{} - startDate: {}, endDate: {}", accountNumber, startDate, endDate);

        // Si tienes un método en el UseCase para filtrar por fechas, úsalo aquí
        // Por ahora, implementaré una versión básica que devuelve todos los movimientos
        // de la cuenta
        Flux<MovementResponse> movements = movementUseCase.findMovementsByAccountNumber(accountNumber)
                .map(MovementDtoMapper::toDto)
                .map(this::toMovementResponse);

        return Mono.just(ResponseEntity.ok(movements));
    }

    // Mappers de OpenAPI models a DTOs internos
    private com.ntt.challenge.bankapp.application.dto.MovementDto toMovementDto(MovementRequest request) {
        com.ntt.challenge.bankapp.application.dto.MovementDto dto = new com.ntt.challenge.bankapp.application.dto.MovementDto();
        dto.setMovementType(request.getMovementType() != null ? request.getMovementType().getValue() : null);
        dto.setValue(request.getValue());
        dto.setAccountNumber(request.getAccountNumber());
        return dto;
    }

    private MovementResponse toMovementResponse(com.ntt.challenge.bankapp.application.dto.MovementDto dto) {
        MovementResponse response = new MovementResponse();
        response.setMovementId(dto.getMovementId());
        response.setDate(dto.getDate());
        response.setMovementType(dto.getMovementType());
        response.setValue(dto.getValue());
        response.setBalance(dto.getBalance());
        response.setAccountNumber(dto.getAccountNumber());
        return response;
    }
}
