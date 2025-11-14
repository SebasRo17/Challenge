package com.ntt.challenge.bankapp.domain.repository;

import com.ntt.challenge.bankapp.domain.model.Movement;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de dominio para operaciones con movimientos.
 */
public interface MovementRepository {
    Optional<Movement> findTopByAccountNumberOrderByDateDesc(String accountNumber);

    List<Movement> findByAccountNumber(String accountNumber);

    Movement save(Movement movement);
}
