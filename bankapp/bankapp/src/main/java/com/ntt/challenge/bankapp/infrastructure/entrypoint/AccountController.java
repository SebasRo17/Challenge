package com.banco.challenge.infrastructure.entrypoint;

import com.banco.challenge.domain.model.Account;
import com.banco.challenge.domain.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor

public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public Flux<Account> getAllAccounts() {
        log.info("GET /api/v1/accounts");
        return accountService.findAllAccounts();
    }

    @GetMapping("/{accountNumber}")
    public Mono<Account> getAccountByNumber(@PathVariable String accountNumber) {
        log.info("GET /api/v1/accounts/{}", accountNumber);
        return accountService.findAccountByNumber(accountNumber);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Account> createAccount(@RequestBody Account account) {
        log.info("POST /api/v1/accounts");
        return accountService.saveAccount(account);
    }

    @PutMapping("/{accountNumber}")
    public Mono<Account> updateAccount(@PathVariable String accountNumber, @RequestBody Account account) {
        log.info("PUT /api/v1/accounts/{}", accountNumber);
        return accountService.updateAccount(accountNumber, account);
    }

    @DeleteMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAccount(@PathVariable String accountNumber) {
        log.info("DELETE /api/v1/accounts/{}", accountNumber);
        return accountService.deleteAccount(accountNumber);
    }
}