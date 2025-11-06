package com.ntt.challenge.bankapp.domain.repository;

import com.ntt.challenge.bankapp.domain.model.Customer;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de dominio para operaciones con clientes.
 */
public interface CustomerRepository {
    List<Customer> findAll();

    Optional<Customer> findById(Long id);

    Optional<Customer> findByIdentification(String identification);

    Customer save(Customer customer);

    void deleteById(Long id);
}
