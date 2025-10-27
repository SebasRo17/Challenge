package com.ntt.challenge.bankapp.application.usecase;

import com.ntt.challenge.bankapp.domain.exception.InsufficientBalanceException;
import com.ntt.challenge.bankapp.domain.model.Account;
import com.ntt.challenge.bankapp.domain.model.Movement;
import com.ntt.challenge.bankapp.domain.service.MovementService;
import com.ntt.challenge.bankapp.infrastructure.repository.AccountJpaRepository;
import com.ntt.challenge.bankapp.infrastructure.repository.MovementJpaRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

@Slf4j
@Service
@Getter
@Setter
@RequiredArgsConstructor

public class MovementUseCase implements MovementService {

    private final MovementJpaRepository movementJpaRepository;
    private final AccountJpaRepository accountJpaRepository;

    private static final String DEBIT_TYPE = "Débito";
    private static final String CREDIT_TYPE = "Crédito";

    @Override
    public Flux<Movement> findMovementsByDateRange(Long customerId, LocalDate startDate, LocalDate endDate) {
        log.info("Finding movements for customer ID: {} from {} to {}", customerId, startDate, endDate);
        return Flux.defer(() -> Flux.fromIterable( // llamada al Query
                movementJpaRepository.findByCustomerAndDateRange(customerId, startDate, endDate)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Movement> saveMovement(Movement movement) {
        log.info("Saving new movement: {}", movement);

        return Mono.fromCallable(() -> {

            if (movement.getValue() <= 0) {
                throw new IllegalArgumentException("Valor del movimiento debe ser mayor a cero");
            }

            // Buscar la cuenta por su número (no por ID), ya que el identificador es el
            // accountNumber
            Account account = accountJpaRepository.findByAccountNumber(movement.getAccount().getAccountNumber())
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

            double currentBalance = movementJpaRepository
                    .findTopByAccount_AccountNumberOrderByDateDesc(account.getAccountNumber())
                    .map(Movement::getBalance)
                    .orElse(account.getInitialBalance());

            log.info("Current balance for account {}: {}", account.getAccountNumber(), currentBalance);

            double newBalance;

            if (DEBIT_TYPE.equalsIgnoreCase(movement.getMovementType())) {

                // REGLA f3
                if (currentBalance < movement.getValue()) {
                    log.warn("Intento de sobregiro en cuenta {}. Saldo: {}, Retiro: {}",
                            account.getAccountNumber(), currentBalance, movement.getValue());
                    throw new InsufficientBalanceException("Saldo no disponible");
                }

                // REGLA f2
                newBalance = currentBalance - movement.getValue();
            }
            // REGLA f2
            else if (CREDIT_TYPE.equalsIgnoreCase(movement.getMovementType())) {
                newBalance = currentBalance + movement.getValue();
            } else {
                throw new IllegalArgumentException("Tipo de movimiento inválido");
            }

            movement.setAccount(account);
            movement.setDate(LocalDate.now());
            movement.setBalance(newBalance);

            log.info("New balance for account {} after {} of {}: {}",
                    account.getAccountNumber(), movement.getMovementType(), movement.getValue(), newBalance);
            return movementJpaRepository.save(movement);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}