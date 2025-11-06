package com.ntt.challenge.bankapp.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MovementDto {
    private Long movementId;

    private LocalDate date;

    @NotBlank
    private String movementType;

    @NotNull
    private Double value;

    private Double balance;

    @NotBlank
    private String accountNumber;
}
