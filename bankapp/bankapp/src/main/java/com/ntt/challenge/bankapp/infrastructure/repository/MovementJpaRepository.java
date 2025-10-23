package com.ntt.challenge.bankapp.infrastructure.repository;

import com.ntt.challenge.bankapp.domain.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Repository
public interface MovementJpaRepository extends JpaRepository<Movement, Long> {

    @Query("SELECT m FROM Movement m " +
            "JOIN m.account a " +
            "JOIN a.customer c " +
            "WHERE c.customerId = :customerId AND m.data BETWEEN :startDate AND :endDate" +
            "ORDER BY m.date ASC")
    List<Movement> findByCustomerAndDateRange(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    Optional<Movement> findTopByAccount_AccountNumberOrderByDateDesc(String accountNumber);
}
