package com.ntt.challenge.bankapp.application.mapper;

import com.ntt.challenge.bankapp.domain.model.Account;
import com.ntt.challenge.bankapp.domain.model.Movement;
import com.ntt.challenge.bankapp.infrastructure.persistence.entity.AccountEntity;
import com.ntt.challenge.bankapp.infrastructure.persistence.entity.MovementEntity;

public class MovementEntityMapper {
    private MovementEntityMapper() {
    }

    public static Movement toDomain(MovementEntity entity) {
        if (entity == null)
            return null;
        Movement m = new Movement();
        m.setMovementId(entity.getMovementId());
        m.setDate(entity.getDate());
        m.setMovementType(entity.getMovementType());
        m.setValue(entity.getValue());
        m.setBalance(entity.getBalance());
        if (entity.getAccount() != null) {
            Account a = new Account();
            a.setAccountNumber(entity.getAccount().getAccountNumber());
            m.setAccount(a);
        }
        return m;
    }

    public static MovementEntity toEntity(Movement domain) {
        if (domain == null)
            return null;
        MovementEntity e = new MovementEntity();
        e.setMovementId(domain.getMovementId());
        e.setDate(domain.getDate());
        e.setMovementType(domain.getMovementType());
        e.setValue(domain.getValue());
        e.setBalance(domain.getBalance());
        if (domain.getAccount() != null) {
            AccountEntity a = new AccountEntity();
            a.setAccountNumber(domain.getAccount().getAccountNumber());
            e.setAccount(a);
        }
        return e;
    }
}
