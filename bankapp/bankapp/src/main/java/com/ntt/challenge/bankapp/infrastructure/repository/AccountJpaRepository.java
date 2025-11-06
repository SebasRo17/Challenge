package com.ntt.challenge.bankapp.infrastructure.repository;

import com.ntt.challenge.bankapp.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByAccountNumber(String accountNumber);

    boolean existsByCustomer_CustomerIdAndAccountType(Long customerId, String accountType);

    List<AccountEntity> findByCustomer_CustomerId(Long customerId);
}