package com.ntt.challenge.bankapp.domain.policy;

import com.ntt.challenge.bankapp.domain.exception.InsufficientBalanceException;

public class DefaultMovementPolicy implements MovementPolicy {
    private static final String DEBIT_TYPE = "Débito";
    private static final String CREDIT_TYPE = "Crédito";

    @Override
    public double calculateNewBalance(double currentBalance, String movementType, double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Valor del movimiento debe ser mayor a cero");
        }
        if (DEBIT_TYPE.equalsIgnoreCase(movementType)) {
            if (currentBalance < value) {
                throw new InsufficientBalanceException("Saldo no disponible");
            }
            return currentBalance - value;
        } else if (CREDIT_TYPE.equalsIgnoreCase(movementType)) {
            return currentBalance + value;
        }
        throw new IllegalArgumentException("Tipo de movimiento inválido");
    }
}
