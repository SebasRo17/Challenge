package com.ntt.challenge.bankapp.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {

    private String name;
    private String gender;

    private String address;
    private String phone;

    private String identification;
}