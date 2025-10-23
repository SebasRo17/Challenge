package com.banco.challenge.infrastructure.entrypoint;

import com.banco.challenge.domain.model.Movement;
import com.banco.challenge.domain.service.MovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/movements") 
@RequiredArgsConstructor

public class MovementController {
    private final MovementService movementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Movement> createMovement(@RequestBody Movement movement) {
        log.info("POST /api/v1/movements");
        return movementService.saveMovement(movement);
    }
}