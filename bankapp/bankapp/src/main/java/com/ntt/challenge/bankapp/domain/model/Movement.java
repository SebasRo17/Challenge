package com.ntt.challenge.bankapp.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@Entity
@Table(name = "movements")

public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movementId;

    private LocalDate date;
    private String movementType; // Puede ser "DEBIT" o "CREDIT"
    private Double value;
    private Double balance;

    // Relacion muchos a uno con Account N-1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number") // Clave foranea
    @JsonBackReference
    private Account account;
}