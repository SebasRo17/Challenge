package com.ntt.challenge.bankapp.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
    @NotBlank
    private String accountNumber;

    @NotBlank
    private String accountType;

    private Double initialBalance;
    private Boolean status;

    private Long customerId;
}
