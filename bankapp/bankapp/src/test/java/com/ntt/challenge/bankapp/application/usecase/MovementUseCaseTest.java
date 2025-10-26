package com.ntt.challenge.bankapp.application.usecase;

import com.ntt.challenge.bankapp.domain.exception.InsufficientBalanceException;
import com.ntt.challenge.bankapp.domain.model.Account;
import com.ntt.challenge.bankapp.domain.model.Movement;
import com.ntt.challenge.bankapp.infrastructure.repository.AccountJpaRepository;
import com.ntt.challenge.bankapp.infrastructure.repository.MovementJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier; // Importante para probar 'Mono'

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class MovementUseCaseTest {

    @Mock
    private AccountJpaRepository accountJpaRepository;

    @Mock
    private MovementJpaRepository movementJpaRepository;

    @InjectMocks // AQUI SE INYECTAN LOS MOCOS EN LA CLASE A PROBAR
    private MovementUseCase movementUseCase;

    private Account testAccount;
    private Movement testMovement;

    @BeforeEach // Se ejecuta antes de cada prueba
    void setUp() {
        // Cuenta de prueba
        testAccount = new Account();
        testAccount.setAccountNumber("478758");
        testAccount.setInitialBalance(2000.0);

        // Movimiento de prueba
        testMovement = new Movement();
        testMovement.setAccount(testAccount);
        testMovement.setMovementType("Débito");
    }

    // PRUEBA 1

    @Test
    void testSaveMovement_SuccessDebit() {
        // DADO QUE
        testMovement.setValue(575.0);

        // SIMULAR QUE SE ENCUENTRA LA CUENTA
        when(accountJpaRepository.findByAccountNumber(eq("478758")))
                .thenReturn(Optional.of(testAccount));

        // SIMULAR QUE NO HAY MOVIMIENTOS PREVIOS
        when(movementJpaRepository.save(any(Movement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // SIMULAR QUE EL GUARDADO ES EXITOSO
        when(movementJpaRepository.save(any(Movement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var monoResult = movementUseCase.saveMovement(testMovement);

        // ENTONCES
        StepVerifier.create(monoResult)
                .assertNext(savedMovement -> {
                    assertNotNull(savedMovement);
                    assertEquals(1425.0, savedMovement.getBalance());
                    assertEquals("Débito", savedMovement.getMovementType());
                })
                .verifyComplete();
    }

    // PRUEBA 2

    @Test
    void testSaveMovement_FailsInsufficientBalance() {
        // DADO QUE
        testMovement.setValue(3000.0);

        // SIMULAR QUE SE ENCUENTRA LA CUENTA
        when(accountJpaRepository.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(testAccount));

        // SIMULAR QUE NO HAY MOVIMIENTOS PREVIOS
        when(movementJpaRepository.findTopByAccount_AccountNumberOrderByDateDesc(anyString()))
                .thenReturn(Optional.empty());

        // CUANDO
        var monoResult = movementUseCase.saveMovement(testMovement);

        // ENTONCES
        StepVerifier.create(monoResult)
                .expectError(InsufficientBalanceException.class)
                .verify();
    }
}
