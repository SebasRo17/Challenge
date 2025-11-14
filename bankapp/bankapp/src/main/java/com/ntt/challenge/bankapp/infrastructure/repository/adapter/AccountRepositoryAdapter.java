package com.ntt.challenge.bankapp.infrastructure.repository.adapter;

import com.ntt.challenge.bankapp.application.mapper.AccountEntityMapper;
import com.ntt.challenge.bankapp.domain.model.Account;
import com.ntt.challenge.bankapp.domain.repository.AccountRepository;
import com.ntt.challenge.bankapp.infrastructure.repository.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    @Override
    public Flux<Account> findAll() {
        return Flux.defer(() -> Flux.fromIterable(accountJpaRepository.findAll()))
                .map(AccountEntityMapper::toDomain)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Account> findByAccountNumber(String accountNumber) {
        return Mono.fromCallable(() -> accountJpaRepository.findByAccountNumber(accountNumber))
                .flatMap(opt -> opt.map(Mono::just).orElse(Mono.empty()))
                .map(AccountEntityMapper::toDomain)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Account> save(Account account) {
        return Mono.fromCallable(() -> {
            var entity = AccountEntityMapper.toEntity(account);
            var savedEntity = accountJpaRepository.save(entity);
            return AccountEntityMapper.toDomain(savedEntity);
        })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Boolean> existsByCustomerIdAndAccountType(Long customerId, String accountType) {
        return Mono
                .fromCallable(
                        () -> accountJpaRepository.existsByCustomer_CustomerIdAndAccountType(customerId, accountType))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<Account> findByCustomerId(Long customerId) {
        return Flux.defer(() -> Flux.fromIterable(accountJpaRepository.findByCustomer_CustomerId(customerId)))
                .map(AccountEntityMapper::toDomain)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> deleteByAccountNumber(String accountNumber) {
        return Mono.fromRunnable(() -> accountJpaRepository.deleteByAccountNumber(accountNumber))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
