package com.ntt.challenge.bankapp.infrastructure.controller;

import com.ntt.challenge.bankapp.application.mapper.AccountDtoMapper;
import com.ntt.challenge.bankapp.application.usecase.AccountUseCase;
import com.ntt.challenge.bankapp.infrastructure.api.CuentasApi;
import com.ntt.challenge.bankapp.infrastructure.api.model.AccountRequest;
import com.ntt.challenge.bankapp.infrastructure.api.model.AccountResponse;
import com.ntt.challenge.bankapp.infrastructure.api.model.AccountUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountApiController implements CuentasApi {

    private final AccountUseCase accountUseCase;

    @Override
    public Mono<ResponseEntity<AccountResponse>> createAccount(
            Mono<AccountRequest> accountRequest,
            ServerWebExchange exchange) {
        log.info("POST /accounts");
        return accountRequest
                .map(this::toAccountDto)
                .flatMap(dto -> accountUseCase.saveAccount(AccountDtoMapper.toDomain(dto)))
                .map(AccountDtoMapper::toDto)
                .map(this::toAccountResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteAccount(String accountNumber, ServerWebExchange exchange) {
        log.info("DELETE /accounts/{}", accountNumber);
        return accountUseCase.deleteAccount(accountNumber)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @Override
    public Mono<ResponseEntity<AccountResponse>> getAccountByNumber(
            String accountNumber,
            ServerWebExchange exchange) {
        log.info("GET /accounts/{}", accountNumber);
        return accountUseCase.findByAccountNumber(accountNumber)
                .map(AccountDtoMapper::toDto)
                .map(this::toAccountResponse)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<AccountResponse>>> getAllAccounts(ServerWebExchange exchange) {
        log.info("GET /accounts");
        Flux<AccountResponse> accounts = accountUseCase.findAllAccounts()
                .map(AccountDtoMapper::toDto)
                .map(this::toAccountResponse);
        return Mono.just(ResponseEntity.ok(accounts));
    }

    @Override
    public Mono<ResponseEntity<AccountResponse>> updateAccount(
            String accountNumber,
            Mono<AccountUpdateRequest> accountUpdateRequest,
            ServerWebExchange exchange) {
        log.info("PUT /accounts/{}", accountNumber);
        return accountUpdateRequest
                .map(this::toAccountDtoForUpdate)
                .flatMap(dto -> accountUseCase.updateAccount(accountNumber, AccountDtoMapper.toDomain(dto)))
                .map(AccountDtoMapper::toDto)
                .map(this::toAccountResponse)
                .map(ResponseEntity::ok);
    }

    // Mappers de OpenAPI models a DTOs internos
    private com.ntt.challenge.bankapp.application.dto.AccountDto toAccountDto(AccountRequest request) {
        com.ntt.challenge.bankapp.application.dto.AccountDto dto = new com.ntt.challenge.bankapp.application.dto.AccountDto();
        dto.setAccountNumber(request.getAccountNumber());
        dto.setAccountType(request.getAccountType() != null ? request.getAccountType().getValue() : null);
        dto.setInitialBalance(request.getInitialBalance());
        dto.setStatus(request.getStatus());
        dto.setCustomerId(request.getCustomerId());
        return dto;
    }

    private com.ntt.challenge.bankapp.application.dto.AccountDto toAccountDtoForUpdate(AccountUpdateRequest request) {
        com.ntt.challenge.bankapp.application.dto.AccountDto dto = new com.ntt.challenge.bankapp.application.dto.AccountDto();
        dto.setAccountType(request.getAccountType() != null ? request.getAccountType().getValue() : null);
        dto.setStatus(request.getStatus());
        return dto;
    }

    private AccountResponse toAccountResponse(com.ntt.challenge.bankapp.application.dto.AccountDto dto) {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber(dto.getAccountNumber());
        response.setAccountType(dto.getAccountType());
        response.setInitialBalance(dto.getInitialBalance());
        response.setStatus(dto.getStatus());
        response.setCustomerId(dto.getCustomerId());
        return response;
    }
}
