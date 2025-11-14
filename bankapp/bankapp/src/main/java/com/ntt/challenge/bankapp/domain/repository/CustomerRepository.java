package com.ntt.challenge.bankapp.domain.repository;

import com.ntt.challenge.bankapp.domain.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de dominio para operaciones con clientes.
 * Retorna tipos reactivos para evitar bloqueo.
 */
public interface CustomerRepository {
    Flux<Customer> findAll();

    Mono<Customer> findById(Long id);

    Mono<Customer> findByIdentification(String identification);

    Mono<Customer> save(Customer customer);

    Mono<Void> deleteById(Long id);
}
