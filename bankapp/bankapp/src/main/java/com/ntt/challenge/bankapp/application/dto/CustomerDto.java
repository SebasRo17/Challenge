package com.ntt.challenge.bankapp.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {
    private Long customerId;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;
    private String gender;

    private String address;
    private String phone;

    @NotBlank(message = "La identificacion no puede estar vacia")
    @Pattern(regexp = "^(\\d{10}|\\d{13})$", message = "La identificación debe tener 10 o 13 dígitos numéricos")
    private String identification;

    @NotBlank(message = "El password no puede estar vacio")
    @Pattern(regexp = "^[0-9]+$", message = "La contraseña debe contener solo números")
    private String password;

    private Boolean status;
}
