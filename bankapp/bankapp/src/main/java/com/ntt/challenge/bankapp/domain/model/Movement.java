package com.ntt.challenge.bankapp.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class Movement {

    private Long movementId;

    private LocalDate date;
    private String movementType; // Puede ser "DEBIT" o "CREDIT"
    private Double value;
    private Double balance;

    private Account account;
}