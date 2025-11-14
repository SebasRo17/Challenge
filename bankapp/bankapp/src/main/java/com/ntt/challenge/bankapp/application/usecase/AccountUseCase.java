package com.ntt.challenge.bankapp.application.usecase;

import com.ntt.challenge.bankapp.domain.exception.AccountTypeAlreadyExistsException;
import com.ntt.challenge.bankapp.domain.model.Account;
import com.ntt.challenge.bankapp.domain.repository.AccountRepository;
import com.ntt.challenge.bankapp.domain.repository.CustomerRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Getter
@Setter
@RequiredArgsConstructor
public class AccountUseCase {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public Flux<Account> findAllAccounts() {
        log.info("Executing findAllAccounts");
        return accountRepository.findAll();
    }

    public Mono<Account> findByAccountNumber(String accountNumber) {
        log.info("Finding account by number: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found")));
    }

    public Mono<Account> saveAccount(Account account) {
        log.info("Saving new account: {}", account);
        return customerRepository.findById(account.getCustomer().getCustomerId())
                .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")))
                .flatMap(customer -> accountRepository.existsByCustomerIdAndAccountType(
                        customer.getCustomerId(),
                        account.getAccountType())
                        .flatMap(exists -> {
                            if (exists) {
                                log.warn("Intento de crear cuenta con tipo duplicado: {} para el cliente ID: {}",
                                        account.getAccountType(), customer.getCustomerId());
                                return Mono.error(new AccountTypeAlreadyExistsException(
                                        "El cliente ya posee una cuenta de tipo '" + account.getAccountType() + "'"));
                            }
                            account.setCustomer(customer);
                            return accountRepository.save(account);
                        }));
    }

    public Mono<Account> updateAccount(String accountNumber, Account accountDetails) {
        log.info("Updating account with number: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found")))
                .flatMap(account -> {
                    account.setAccountType(accountDetails.getAccountType());
                    account.setStatus(accountDetails.getStatus());
                    return accountRepository.save(account);
                });
    }

    public Mono<Void> deleteAccount(String accountNumber) {
        log.info("Executing deleteAccount for account number: {}", accountNumber);
        return accountRepository.deleteByAccountNumber(accountNumber);
    }

    public Flux<Account> findAccountsByCustomerId(Long customerId) {
        log.info("Finding account by customer ID: {}", customerId);
        return accountRepository.findByCustomerId(customerId);
    }
}