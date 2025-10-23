package com.ntt.challenge.bankapp.domain.service;

import com.ntt.challenge.bankapp.domain.model.Account;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface AccountService {
    Flux<Account> findAllAccounts();

    Mono<Account> findByAccountNumber(String accountNumber);

    Mono<Account> saveAccount(Account account);

    Mono<Account> updateAccount(String accountNumber, Account account);

    Mono<Void> deleteAccount(String accountNumber);

    Flux<Account> findAccountsByCustomerId(Long customerId);
}