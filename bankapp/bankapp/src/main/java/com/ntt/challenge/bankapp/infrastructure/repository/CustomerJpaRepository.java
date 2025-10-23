package com.ntt.challenge.bankapp.infrastructure.repository;

import com.ntt.challenge.bankapp.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerJpaRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByIdentification(String identification);
}