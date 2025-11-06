package com.ntt.challenge.bankapp.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Customer extends Person {

    private Long customerId;

    private String password;

    private Boolean status;

    private List<Account> accounts;
}
