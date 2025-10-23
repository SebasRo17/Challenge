package com.ntt.challenge.bankapp.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "accounts")

public class Account {

    // Se pide que tenga número, tipo, saldo Inicial, estado
    @Id
    @Column(name = "account_number", unique = true)
    private String accountNumber;

    private String accountType;
    private Double initialBalance;
    private Boolean status;

    // Relacion muchos a uno con Customer N-1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id") // Clave foranea
    @JsonBackReference
    private Customer customer;

    // Relacion uno a muchos con Movement 1-N
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Movement> movements;
}
