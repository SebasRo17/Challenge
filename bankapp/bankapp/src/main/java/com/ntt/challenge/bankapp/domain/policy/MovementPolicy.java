package com.ntt.challenge.bankapp.domain.policy;

import com.ntt.challenge.bankapp.domain.exception.InsufficientBalanceException;

public interface MovementPolicy {
    /**
     * Calcula el nuevo saldo según el tipo de movimiento.
     * 
     * @throws InsufficientBalanceException si un débito excede el saldo actual
     * @throws IllegalArgumentException     si el tipo es inválido o el valor no es
     *                                      positivo
     */
    double calculateNewBalance(double currentBalance, String movementType, double value);
}
