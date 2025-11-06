package com.ntt.challenge.bankapp.application.usecase;

import com.ntt.challenge.bankapp.domain.model.Account;
import com.ntt.challenge.bankapp.domain.model.Movement;
import com.ntt.challenge.bankapp.domain.policy.MovementPolicy;
import com.ntt.challenge.bankapp.domain.repository.AccountRepository;
import com.ntt.challenge.bankapp.domain.repository.MovementRepository;
import com.ntt.challenge.bankapp.domain.service.MovementService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

@Slf4j
@Service
@Getter
@Setter
@RequiredArgsConstructor
public class MovementUseCase implements MovementService {

        private final MovementRepository movementRepository;
        private final AccountRepository accountRepository;
        private final MovementPolicy movementPolicy;

        @Override
        public Mono<Movement> saveMovement(Movement movement) {
                log.info("Saving new movement: {}", movement);

                return Mono.fromCallable(() -> {
                        // Buscar la cuenta por su número (no por ID), ya que el identificador es el
                        // accountNumber
                        Account account = accountRepository
                                        .findByAccountNumber(movement.getAccount().getAccountNumber())
                                        .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

                        double currentBalance = movementRepository
                                        .findTopByAccountNumberOrderByDateDesc(account.getAccountNumber())
                                        .map(Movement::getBalance)
                                        .orElse(account.getInitialBalance());

                        log.info("Current balance for account {}: {}", account.getAccountNumber(), currentBalance);

                        double newBalance = movementPolicy.calculateNewBalance(currentBalance,
                                        movement.getMovementType(), movement.getValue());

                        movement.setAccount(account);
                        movement.setDate(LocalDate.now());
                        movement.setBalance(newBalance);

                        log.info("New balance for account {} after {} of {}: {}",
                                        account.getAccountNumber(), movement.getMovementType(), movement.getValue(),
                                        newBalance);
                        return movementRepository.save(movement);
                }).subscribeOn(Schedulers.boundedElastic());
        }
}