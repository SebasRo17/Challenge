package com.ntt.challenge.bankapp.domain.model;

import jakarta.persistence.*;
import lambok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "accounts")

public class Account {

    //Se pide que tenga número, tipo, saldo Inicial, estado
    @Id
    @Column(name= "account_number", unique = true)
    private String accountNumber;

    private String accountType;
    private Double initialBalance;
    private Boolean status;
}
