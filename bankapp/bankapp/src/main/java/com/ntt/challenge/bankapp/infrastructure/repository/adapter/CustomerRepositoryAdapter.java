package com.ntt.challenge.bankapp.infrastructure.repository.adapter;

import com.ntt.challenge.bankapp.application.mapper.CustomerEntityMapper;
import com.ntt.challenge.bankapp.domain.model.Customer;
import com.ntt.challenge.bankapp.domain.repository.CustomerRepository;
import com.ntt.challenge.bankapp.infrastructure.repository.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public Flux<Customer> findAll() {
        return Flux.defer(() -> Flux.fromIterable(customerJpaRepository.findAll()))
                .map(CustomerEntityMapper::toDomain)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Customer> findById(Long id) {
        return Mono.fromCallable(() -> customerJpaRepository.findById(id))
                .flatMap(opt -> opt.map(Mono::just).orElse(Mono.empty()))
                .map(CustomerEntityMapper::toDomain)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Customer> findByIdentification(String identification) {
        return Mono.fromCallable(() -> customerJpaRepository.findByIdentification(identification))
                .flatMap(opt -> opt.map(Mono::just).orElse(Mono.empty()))
                .map(CustomerEntityMapper::toDomain)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Customer> save(Customer customer) {
        return Mono.fromCallable(() -> {
            var entity = CustomerEntityMapper.toEntity(customer);
            var saved = customerJpaRepository.save(entity);
            return CustomerEntityMapper.toDomain(saved);
        })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return Mono.fromRunnable(() -> customerJpaRepository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
