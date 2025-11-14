package com.ntt.challenge.bankapp.infrastructure.repository.adapter;

import com.ntt.challenge.bankapp.application.mapper.MovementEntityMapper;
import com.ntt.challenge.bankapp.domain.model.Movement;
import com.ntt.challenge.bankapp.domain.repository.MovementRepository;
import com.ntt.challenge.bankapp.infrastructure.repository.MovementJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
@RequiredArgsConstructor
public class MovementRepositoryAdapter implements MovementRepository {

    private final MovementJpaRepository movementJpaRepository;

    @Override
    public Mono<Movement> findTopByAccountNumberOrderByDateDesc(String accountNumber) {
        return Mono
                .fromCallable(() -> movementJpaRepository.findTopByAccount_AccountNumberOrderByDateDesc(accountNumber))
                .flatMap(opt -> opt.map(Mono::just).orElse(Mono.empty()))
                .map(MovementEntityMapper::toDomain)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<Movement> findByAccountNumber(String accountNumber) {
        return Flux.defer(() -> Flux.fromIterable(movementJpaRepository.findByAccount_AccountNumber(accountNumber)))
                .map(MovementEntityMapper::toDomain)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Movement> save(Movement movement) {
        return Mono.fromCallable(() -> {
            var entity = MovementEntityMapper.toEntity(movement);
            var saved = movementJpaRepository.save(entity);
            return MovementEntityMapper.toDomain(saved);
        })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
