package com.ntt.challenge.bankapp.domain.repository;

import com.ntt.challenge.bankapp.domain.model.Account;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de dominio para operaciones con cuentas.
 * No depende de frameworks.
 */
public interface AccountRepository {
    List<Account> findAll();

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByCustomerIdAndAccountType(Long customerId, String accountType);

    List<Account> findByCustomerId(Long customerId);

    Account save(Account account);

    void deleteByAccountNumber(String accountNumber);
}
