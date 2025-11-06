package com.ntt.challenge.bankapp.infrastructure.repository;

import com.ntt.challenge.bankapp.infrastructure.persistence.entity.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Repository
public interface MovementJpaRepository extends JpaRepository<MovementEntity, Long> {

        @Query("SELECT m FROM MovementEntity m " +
                        "JOIN m.account a " +
                        "JOIN a.customer c " +
                        "WHERE c.customerId = :customerId AND m.date BETWEEN :startDate AND :endDate " +
                        "ORDER BY m.date ASC")
        List<MovementEntity> findByCustomerAndDateRange(
                        @Param("customerId") Long customerId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        Optional<MovementEntity> findTopByAccount_AccountNumberOrderByDateDesc(String accountNumber);
}
