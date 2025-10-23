package com.ntt.challenge.bankapp.domain.service;

import com.ntt.challenge.bankapp.domain.model.Movement;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.time.LocalDate;

public interface MovementService {
    Mono<Movement> saveMovement(Movement movement);
    Flux<Movement> findMovementsByDateRange(Long customerId, LocalDate startDate, LocalDate endDate);
}