package com.ntt.challenge.bankapp.infrastructure.repository;

import com.ntt.challenge.bankapp.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AccountJpaRepository extends JpaRepository<Account, String> {
    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByCustomer_CustomerId(Long customerId);
}