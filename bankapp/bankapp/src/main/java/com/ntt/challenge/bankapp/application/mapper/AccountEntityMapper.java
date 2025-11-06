package com.ntt.challenge.bankapp.application.mapper;

import com.ntt.challenge.bankapp.domain.model.Account;
import com.ntt.challenge.bankapp.domain.model.Customer;
import com.ntt.challenge.bankapp.infrastructure.persistence.entity.AccountEntity;
import com.ntt.challenge.bankapp.infrastructure.persistence.entity.CustomerEntity;

public class AccountEntityMapper {
    private AccountEntityMapper() {
    }

    public static Account toDomain(AccountEntity entity) {
        if (entity == null)
            return null;
        Account a = new Account();
        a.setAccountNumber(entity.getAccountNumber());
        a.setAccountType(entity.getAccountType());
        a.setInitialBalance(entity.getInitialBalance());
        a.setStatus(entity.getStatus());
        if (entity.getCustomer() != null) {
            Customer c = new Customer();
            c.setCustomerId(entity.getCustomer().getCustomerId());
            c.setName(entity.getCustomer().getName());
            c.setIdentification(entity.getCustomer().getIdentification());
            a.setCustomer(c);
        }
        return a;
    }

    public static AccountEntity toEntity(Account domain) {
        if (domain == null)
            return null;
        AccountEntity e = new AccountEntity();
        e.setAccountNumber(domain.getAccountNumber());
        e.setAccountType(domain.getAccountType());
        e.setInitialBalance(domain.getInitialBalance());
        e.setStatus(domain.getStatus());
        if (domain.getCustomer() != null) {
            CustomerEntity c = new CustomerEntity();
            c.setCustomerId(domain.getCustomer().getCustomerId());
            e.setCustomer(c);
        }
        return e;
    }
}
