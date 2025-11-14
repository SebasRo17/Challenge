package com.ntt.challenge.bankapp.domain.repository;

import com.ntt.challenge.bankapp.domain.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de dominio para operaciones con movimientos.
 * Retorna tipos reactivos para evitar bloqueo.
 */
public interface MovementRepository {
    Mono<Movement> findTopByAccountNumberOrderByDateDesc(String accountNumber);

    Flux<Movement> findByAccountNumber(String accountNumber);

    Mono<Movement> save(Movement movement);
}
