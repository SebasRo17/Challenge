package com.ntt.challenge.bankapp.domain.model;

import jakarta.persistence.*;
import lambok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "movements")

public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movementId;

    private LocalDate date;
    private String movementType; //Puede ser "DEBIT" o "CREDIT"
    private Double value;
    private Double balance;

}