package com.ntt.challenge.bankapp.application.mapper;

import com.ntt.challenge.bankapp.application.dto.AccountDto;
import com.ntt.challenge.bankapp.domain.model.Account;
import com.ntt.challenge.bankapp.domain.model.Customer;

public class AccountDtoMapper {
    private AccountDtoMapper() {
    }

    public static Account toDomain(AccountDto dto) {
        if (dto == null)
            return null;
        Account a = new Account();
        a.setAccountNumber(dto.getAccountNumber());
        a.setAccountType(dto.getAccountType());
        a.setInitialBalance(dto.getInitialBalance());
        a.setStatus(dto.getStatus());
        if (dto.getCustomerId() != null) {
            Customer c = new Customer();
            c.setCustomerId(dto.getCustomerId());
            a.setCustomer(c);
        }
        return a;
    }

    public static AccountDto toDto(Account domain) {
        if (domain == null)
            return null;
        AccountDto dto = new AccountDto();
        dto.setAccountNumber(domain.getAccountNumber());
        dto.setAccountType(domain.getAccountType());
        dto.setInitialBalance(domain.getInitialBalance());
        dto.setStatus(domain.getStatus());
        if (domain.getCustomer() != null) {
            dto.setCustomerId(domain.getCustomer().getCustomerId());
        }
        return dto;
    }
}
