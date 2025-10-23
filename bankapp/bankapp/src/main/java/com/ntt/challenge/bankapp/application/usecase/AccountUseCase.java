package com.banco.challenge.application.usecase;

import com.banco.challenge.domain.model.Account;
import com.banco.challenge.domain.model.Customer;
import com.banco.challenge.domain.service.AccountService;
import com.banco.challenge.infrastructure.repository.AccountJpaRepository;
import com.banco.challenge.infrastructure.repository.CustomerJpaRepository; 
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
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
    public Mono<Account> findAccountByNumber(String accountNumber) {
        log.info("Finding account by number: {}", accountNumber);
        return Mono.fromCallable(() -> accountJpaRepository.findAccountByNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found")))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Account> saveAccount(Account account) {
        log.info("Saving new account: {}", account);
        return Mono.fromCallable(()-> {

            Customer customer = customerJpaRepository.findById(account.getCustomer().getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            account.setCustomer(customer);

            return accountJpaRepository.save(account);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Account> updateAccount(String accountNumber, Account accountDetails) {
        log.info("Updating account with number: {}", accountNumber);
        return Mono.fromCallable(() -> {
            Account account = accountJpaRepository.findAccountByNumber(accountNumber)
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
    public Mono<Account> findByAccountByCustomerId(Long customerId) {
        log.info("Finding account by customer ID: {}", customerId);
        return Flux.defer(() -> Flux.fromIterable(accountJpaRepository.findByCustomer_CustomerId(customerId)))
                .subscribeOn(Schedulers.boundedElastic());
    }
}