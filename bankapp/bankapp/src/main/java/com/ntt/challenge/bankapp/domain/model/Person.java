package com.ntt.challenge.bankapp.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass //Se utiliza para indicar que no es una tabla en la base de datos, sino una clase padre

public class Person{
    
    private String name;
    private String gender;

    @Column(unique = true) //Esto porque la identificacion siempre debe ser unica
    private String identification;

    private String address;
    private String phone;
}