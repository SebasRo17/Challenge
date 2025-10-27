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
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovementUseCaseTest {

    @Mock
    private AccountJpaRepository accountJpaRepository;

    @Mock
    private MovementJpaRepository movementJpaRepository;

    @InjectMocks
    private MovementUseCase movementUseCase;

    private Account testAccount;
    private Movement testMovement;

    @BeforeEach
    void setUp() {
        // Cuenta de prueba
        testAccount = new Account();
        testAccount.setAccountNumber("478758");
        testAccount.setInitialBalance(2000.0);

        // Movimiento de prueba
        testMovement = new Movement();
        testMovement.setAccount(testAccount); // ¡Clave que la cuenta esté aquí!
        testMovement.setMovementType("Débito");
    }

    // PRUEBA 1
    @Test
    void testSaveMovement_SuccessDebit() {
        // DADO QUE
        testMovement.setValue(575.0);

        // Simulamos que el repo SÍ encuentra la cuenta
        when(accountJpaRepository.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(testAccount));

        // Simulamos que NO hay movimientos previos
        when(movementJpaRepository.findTopByAccount_AccountNumberOrderByDateDesc(anyString()))
                .thenReturn(Optional.empty());

        // Simulamos que el guardado (save) funciona
        when(movementJpaRepository.save(any(Movement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // CUANDO
        var monoResult = movementUseCase.saveMovement(testMovement);

        // ENOTNCES
        StepVerifier.create(monoResult)
                .assertNext(savedMovement -> {
                    assertNotNull(savedMovement);
                    assertEquals(1425.0, savedMovement.getBalance());
                })
                .verifyComplete();
    }

    // PRUEBA 2
    @Test
    void testSaveMovement_FailsInsufficientBalance() {
        // DADO QUE
        testMovement.setValue(3000.0);

        // Simulamos que el repo SÍ encuentra la cuenta
        when(accountJpaRepository.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(testAccount));

        // Simulamos que no hay movimientos previos
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