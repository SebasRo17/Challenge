package com.ntt.challenge.bankapp.domain.repository;

import com.ntt.challenge.bankapp.domain.model.Movement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de dominio para operaciones con movimientos.
 */
public interface MovementRepository {
    List<Movement> findByCustomerAndDateRange(Long customerId, LocalDate startDate, LocalDate endDate);

    Optional<Movement> findTopByAccountNumberOrderByDateDesc(String accountNumber);

    Movement save(Movement movement);
}
