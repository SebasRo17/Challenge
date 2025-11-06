package com.ntt.challenge.bankapp.infrastructure.config;

import com.ntt.challenge.bankapp.domain.policy.DefaultMovementPolicy;
import com.ntt.challenge.bankapp.domain.policy.MovementPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public MovementPolicy movementPolicy() {
        return new DefaultMovementPolicy();
    }
}
