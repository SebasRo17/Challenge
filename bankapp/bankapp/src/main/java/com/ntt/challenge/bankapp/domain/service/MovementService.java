package com.ntt.challenge.bankapp.domain.service;

import com.ntt.challenge.bankapp.domain.model.Movement;
import reactor.core.publisher.Mono;

public interface MovementService {
    Mono<Movement> saveMovement(Movement movement);
}