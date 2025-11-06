package com.ntt.challenge.bankapp.infrastructure.repository.adapter;

import com.ntt.challenge.bankapp.application.mapper.AccountEntityMapper;
import com.ntt.challenge.bankapp.domain.model.Account;
import com.ntt.challenge.bankapp.domain.repository.AccountRepository;
import com.ntt.challenge.bankapp.infrastructure.persistence.entity.AccountEntity;
import com.ntt.challenge.bankapp.infrastructure.repository.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    @Override
    public List<Account> findAll() {
        return accountJpaRepository.findAll().stream()
                .map(AccountEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountJpaRepository.findByAccountNumber(accountNumber)
                .map(AccountEntityMapper::toDomain);
    }

    @Override
    public boolean existsByCustomerIdAndAccountType(Long customerId, String accountType) {
        return accountJpaRepository.existsByCustomer_CustomerIdAndAccountType(customerId, accountType);
    }

    @Override
    public List<Account> findByCustomerId(Long customerId) {
        return accountJpaRepository.findByCustomer_CustomerId(customerId).stream()
                .map(AccountEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Account save(Account account) {
        AccountEntity entity = AccountEntityMapper.toEntity(account);
        AccountEntity saved = accountJpaRepository.save(entity);
        return AccountEntityMapper.toDomain(saved);
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        accountJpaRepository.deleteById(accountNumber);
    }
}
