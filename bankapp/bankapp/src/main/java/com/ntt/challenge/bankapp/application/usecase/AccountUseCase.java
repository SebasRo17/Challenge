package com.ntt.challenge.bankapp.application.usecase;

import com.ntt.challenge.bankapp.domain.model.Customer;
import com.ntt.challenge.bankapp.domain.service.AccountService;
import com.ntt.challenge.bankapp.infrastructure.repository.AccountJpaRepository;
import com.ntt.challenge.bankapp.infrastructure.repository.CustomerJpaRepository;
import com.ntt.challenge.bankapp.domain.model.Account;
import com.ntt.challenge.bankapp.domain.exception.AccountTypeAlreadyExistsException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@Getter
@Setter
@RequiredArgsConstructor

public class AccountUseCase implements AccountService {

    private final AccountJpaRepository accountJpaRepository;
    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public Flux<Account> findAllAccounts() {
        log.info("Executing findAllAccounts");
        return Flux.defer(() -> Flux.fromIterable(accountJpaRepository.findAll()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Account> findByAccountNumber(String accountNumber) {
        log.info("Finding account by number: {}", accountNumber);
        return Mono.fromCallable(() -> accountJpaRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found")))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Account> saveAccount(Account account) {
        log.info("Saving new account: {}", account);
        return Mono.fromCallable(() -> {

            Customer customer = customerJpaRepository.findById(account.getCustomer().getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            boolean exists = accountJpaRepository.existsByCustomer_CustomerIdAndAccountType(
                    customer.getCustomerId(),
                    account.getAccountType());
            if (exists) {
                log.warn("Intento de crear cuenta con tipo duplicado: {} para el cliente ID: {}",
                        account.getAccountType(), customer.getCustomerId());
                throw new AccountTypeAlreadyExistsException(
                        "El cliente ya posee una cuenta de tipo '" + account.getAccountType() + "'");
            }

            account.setCustomer(customer);

            return accountJpaRepository.save(account);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Account> updateAccount(String accountNumber, Account accountDetails) {
        log.info("Updating account with number: {}", accountNumber);
        return Mono.fromCallable(() -> {
            Account account = accountJpaRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            account.setAccountType(accountDetails.getAccountType());
            account.setStatus(accountDetails.getStatus());

            return accountJpaRepository.save(account);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> deleteAccount(String accountNumber) {
        log.info("Executing deleteAccount for account number: {}", accountNumber);
        // Ojo: JPA usa el ID (PK) para borrar, que es 'accountNumber' (String)
        return Mono.fromRunnable(() -> accountJpaRepository.deleteById(accountNumber))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Flux<Account> findAccountsByCustomerId(Long customerId) {
        log.info("Finding account by customer ID: {}", customerId);
        return Flux.defer(() -> Flux.fromIterable(accountJpaRepository.findByCustomer_CustomerId(customerId)))
                .subscribeOn(Schedulers.boundedElastic());
    }
}