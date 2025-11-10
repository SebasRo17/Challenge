package com.ntt.challenge.bankapp.domain.policy;

import com.ntt.challenge.bankapp.domain.exception.InsufficientBalanceException;
import java.text.Normalizer;

public class DefaultMovementPolicy implements MovementPolicy {
    private static final String DEBIT = "DEBIT";
    private static final String CREDIT = "CREDIT";

    private static String normalize(String s) {
        if (s == null)
            throw new IllegalArgumentException("Movement type is required");
        String ascii = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        return ascii.trim().toUpperCase();
    }

    @Override
    public double calculateNewBalance(double currentBalance, String movementType, double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Movement value must be greater than zero");
        }

        String type = normalize(movementType);

        if (DEBIT.equals(type) || "DEBITO".equals(type)) {
            if (currentBalance < value) {
                throw new InsufficientBalanceException("Insufficient balance");
            }
            return currentBalance - value;
        } else if (CREDIT.equals(type) || "CREDITO".equals(type)) {
            return currentBalance + value;
        }

        throw new IllegalArgumentException("Invalid movement type");
    }
}
