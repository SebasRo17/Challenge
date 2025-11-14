package com.ntt.challenge.bankapp.infrastructure.repository;

import com.ntt.challenge.bankapp.infrastructure.persistence.entity.MovementEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovementJpaRepository extends JpaRepository<MovementEntity, Long> {

        @EntityGraph(attributePaths = { "account", "account.customer" })
        List<MovementEntity> findAll();

        @EntityGraph(attributePaths = { "account", "account.customer" })
        Optional<MovementEntity> findTopByAccount_AccountNumberOrderByDateDesc(String accountNumber);

        @EntityGraph(attributePaths = { "account", "account.customer" })
        List<MovementEntity> findByAccount_AccountNumber(String accountNumber);
}
