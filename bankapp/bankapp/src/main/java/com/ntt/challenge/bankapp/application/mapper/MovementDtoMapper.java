package com.ntt.challenge.bankapp.application.mapper;

import com.ntt.challenge.bankapp.application.dto.MovementDto;
import com.ntt.challenge.bankapp.domain.model.Account;
import com.ntt.challenge.bankapp.domain.model.Movement;

public class MovementDtoMapper {
    private MovementDtoMapper() {
    }

    public static Movement toDomain(MovementDto dto) {
        if (dto == null)
            return null;
        Movement m = new Movement();
        m.setMovementId(dto.getMovementId());
        m.setDate(dto.getDate());
        m.setMovementType(dto.getMovementType());
        m.setValue(dto.getValue());
        m.setBalance(dto.getBalance());
        if (dto.getAccountNumber() != null) {
            Account a = new Account();
            a.setAccountNumber(dto.getAccountNumber());
            m.setAccount(a);
        }
        return m;
    }

    public static MovementDto toDto(Movement domain) {
        if (domain == null)
            return null;
        MovementDto dto = new MovementDto();
        dto.setMovementId(domain.getMovementId());
        dto.setDate(domain.getDate());
        dto.setMovementType(domain.getMovementType());
        dto.setValue(domain.getValue());
        dto.setBalance(domain.getBalance());
        if (domain.getAccount() != null) {
            dto.setAccountNumber(domain.getAccount().getAccountNumber());
        }
        return dto;
    }
}
