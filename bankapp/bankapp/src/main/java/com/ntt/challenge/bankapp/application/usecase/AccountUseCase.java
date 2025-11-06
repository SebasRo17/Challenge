package com.ntt.challenge.bankapp.application.usecase;

import com.ntt.challenge.bankapp.domain.exception.AccountTypeAlreadyExistsException;
import com.ntt.challenge.bankapp.domain.model.Account;
import com.ntt.challenge.bankapp.domain.repository.AccountRepository;
import com.ntt.challenge.bankapp.domain.repository.CustomerRepository;
import com.ntt.challenge.bankapp.domain.service.AccountService;
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

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Flux<Account> findAllAccounts() {
        log.info("Executing findAllAccounts");
        return Flux.defer(() -> Flux.fromIterable(accountRepository.findAll()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Account> findByAccountNumber(String accountNumber) {
        log.info("Finding account by number: {}", accountNumber);
        return Mono.fromCallable(() -> accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found")))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Account> saveAccount(Account account) {
        log.info("Saving new account: {}", account);
        return Mono.fromCallable(() -> {
            var customer = customerRepository.findById(account.getCustomer().getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            boolean exists = accountRepository.existsByCustomerIdAndAccountType(
                    customer.getCustomerId(),
                    account.getAccountType());
            if (exists) {
                log.warn("Intento de crear cuenta con tipo duplicado: {} para el cliente ID: {}",
                        account.getAccountType(), customer.getCustomerId());
                throw new AccountTypeAlreadyExistsException(
                        "El cliente ya posee una cuenta de tipo '" + account.getAccountType() + "'");
            }

            account.setCustomer(customer);
            return accountRepository.save(account);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Account> updateAccount(String accountNumber, Account accountDetails) {
        log.info("Updating account with number: {}", accountNumber);
        return Mono.fromCallable(() -> {
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            account.setAccountType(accountDetails.getAccountType());
            account.setStatus(accountDetails.getStatus());

            return accountRepository.save(account);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> deleteAccount(String accountNumber) {
        log.info("Executing deleteAccount for account number: {}", accountNumber);
        return Mono.fromRunnable(() -> accountRepository.deleteByAccountNumber(accountNumber))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Flux<Account> findAccountsByCustomerId(Long customerId) {
        log.info("Finding account by customer ID: {}", customerId);
        return Flux.defer(() -> Flux.fromIterable(accountRepository.findByCustomerId(customerId)))
                .subscribeOn(Schedulers.boundedElastic());
    }
}