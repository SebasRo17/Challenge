package com.ntt.challenge.bankapp.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@MappedSuperclass // Se utiliza para indicar que no es una tabla en la base de datos, sino una
                  // clase padre

public class Person {

    private String name;
    private String gender;

    private String address;
    private String phone;

    @NotBlank(message = "La identificacion no puede estar vacia")
    @Pattern(regexp = "^(\\d{10}|\\d{13})$", message = "La identificación debe tener 10 o 13 dígitos numéricos")
    @Column(unique = true) // Esto porque la identificacion siempre debe ser unica
    private String identification;
}