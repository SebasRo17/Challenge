package com.ntt.challenge.bankapp.domain.model;

import jakarta.persistence.*;
import lambok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Getter
@Setter
@Entity //Una tabla en la base de datos
@Table(name = "customers") //Nombre de la tabla en la base de datos

public class Customer extends Person {//Herencia
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Generacion automatica del id
    private Long customerId;

    private String password;

    @Column(name = "status") 
    private Boolean status;

    //Relacion uno a muchos con Account 1-N
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Account> accounts;
}
