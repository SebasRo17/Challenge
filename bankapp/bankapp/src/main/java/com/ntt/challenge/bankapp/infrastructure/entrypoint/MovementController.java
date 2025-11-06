package com.ntt.challenge.bankapp.infrastructure.entrypoint;

import com.ntt.challenge.bankapp.application.dto.MovementDto;
import com.ntt.challenge.bankapp.application.mapper.MovementDtoMapper;
import com.ntt.challenge.bankapp.domain.service.MovementService;
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
    public Mono<MovementDto> createMovement(@RequestBody MovementDto movement) {
        log.info("POST /api/v1/movements");
        return movementService.saveMovement(MovementDtoMapper.toDomain(movement))
                .map(MovementDtoMapper::toDto);
    }
}