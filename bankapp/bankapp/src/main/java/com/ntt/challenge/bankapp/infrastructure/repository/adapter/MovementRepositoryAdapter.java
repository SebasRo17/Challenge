package com.ntt.challenge.bankapp.infrastructure.repository.adapter;

import com.ntt.challenge.bankapp.application.mapper.MovementEntityMapper;
import com.ntt.challenge.bankapp.domain.model.Movement;
import com.ntt.challenge.bankapp.domain.repository.MovementRepository;
import com.ntt.challenge.bankapp.infrastructure.persistence.entity.MovementEntity;
import com.ntt.challenge.bankapp.infrastructure.repository.MovementJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovementRepositoryAdapter implements MovementRepository {

    private final MovementJpaRepository movementJpaRepository;

    @Override
    public Optional<Movement> findTopByAccountNumberOrderByDateDesc(String accountNumber) {
        return movementJpaRepository.findTopByAccount_AccountNumberOrderByDateDesc(accountNumber)
                .map(MovementEntityMapper::toDomain);
    }

    @Override
    public List<Movement> findByAccountNumber(String accountNumber) {
        return movementJpaRepository.findByAccount_AccountNumber(accountNumber)
                .stream()
                .map(MovementEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Movement save(Movement movement) {
        MovementEntity entity = MovementEntityMapper.toEntity(movement);
        MovementEntity saved = movementJpaRepository.save(entity);
        return MovementEntityMapper.toDomain(saved);
    }
}
