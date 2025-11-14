package com.ntt.challenge.bankapp.domain.repository;

import com.ntt.challenge.bankapp.domain.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de dominio para operaciones con cuentas.
 * Retorna tipos reactivos para evitar bloqueo.
 */
public interface AccountRepository {
    Flux<Account> findAll();

    Mono<Account> findByAccountNumber(String accountNumber);

    Mono<Boolean> existsByCustomerIdAndAccountType(Long customerId, String accountType);

    Flux<Account> findByCustomerId(Long customerId);

    Mono<Account> save(Account account);

    Mono<Void> deleteByAccountNumber(String accountNumber);
}
