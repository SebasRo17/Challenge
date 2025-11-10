package com.ntt.challenge.bankapp.infrastructure.repository;

import com.ntt.challenge.bankapp.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity, String> {

    @EntityGraph(attributePaths = { "customer" })
    List<AccountEntity> findAll();

    @EntityGraph(attributePaths = { "customer" })
    Optional<AccountEntity> findByAccountNumber(String accountNumber);

    boolean existsByCustomer_CustomerIdAndAccountType(Long customerId, String accountType);

    @EntityGraph(attributePaths = { "customer" })
    List<AccountEntity> findByCustomer_CustomerId(Long customerId);

    void deleteByAccountNumber(String accountNumber);
}