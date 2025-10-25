package com.ntt.challenge.bankapp.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@Entity // Una tabla en la base de datos
@Table(name = "customers") // Nombre de la tabla en la base de datos

public class Customer extends Person {// Herencia

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generacion automatica del id
    private Long customerId;

    @NotBlank(message = "El password no puede estar vacio")
    @Pattern(regexp = "^[0-9]+$", message = "La contraseña debe contener solo números")
    private String password;

    @Column(name = "status")
    private Boolean status;

    // Relacion uno a muchos con Account 1-N
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Account> accounts;
}
