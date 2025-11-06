package com.ntt.challenge.bankapp.infrastructure.entrypoint;

import com.ntt.challenge.bankapp.application.dto.AccountDto;
import com.ntt.challenge.bankapp.application.mapper.AccountDtoMapper;
import com.ntt.challenge.bankapp.domain.service.AccountService;
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
    public Flux<AccountDto> getAllAccounts() {
        log.info("GET /api/v1/accounts");
        return accountService.findAllAccounts()
                .map(AccountDtoMapper::toDto);
    }

    @GetMapping("/{accountNumber}")
    public Mono<AccountDto> getAccountByNumber(@PathVariable String accountNumber) {
        log.info("GET /api/v1/accounts/{}", accountNumber);
        return accountService.findByAccountNumber(accountNumber)
                .map(AccountDtoMapper::toDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        log.info("POST /api/v1/accounts");
        return accountService.saveAccount(AccountDtoMapper.toDomain(accountDto))
                .map(AccountDtoMapper::toDto);
    }

    @PutMapping("/{accountNumber}")
    public Mono<AccountDto> updateAccount(@PathVariable String accountNumber, @RequestBody AccountDto accountDto) {
        log.info("PUT /api/v1/accounts/{}", accountNumber);
        return accountService.updateAccount(accountNumber, AccountDtoMapper.toDomain(accountDto))
                .map(AccountDtoMapper::toDto);
    }

    @DeleteMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAccount(@PathVariable String accountNumber) {
        log.info("DELETE /api/v1/accounts/{}", accountNumber);
        return accountService.deleteAccount(accountNumber);
    }
}