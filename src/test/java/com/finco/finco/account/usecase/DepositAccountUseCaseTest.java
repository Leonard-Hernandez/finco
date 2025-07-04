package com.finco.finco.account.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.account.DepositAccountUseCase;
import com.finco.finco.usecase.account.dto.IAccountTransactionData;

@ExtendWith(MockitoExtension.class)
public class DepositAccountUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private AuthGateway authGateway;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private IAccountTransactionData transactionData;

    private DepositAccountUseCase depositAccountUseCase;
    private Account testAccount;
    private User testUser;
    private final Long accountId = 1L;
    private final Long userId = 1L;
    private final BigDecimal initialBalance = BigDecimal.valueOf(1000);
    private final BigDecimal depositAmount = BigDecimal.valueOf(500);
    private BigDecimal expectedAccountBalance = BigDecimal.valueOf(1475);

    @BeforeEach
    public void setUp() {
        depositAccountUseCase = new DepositAccountUseCase(accountGateway, authGateway, transactionGateway);
        
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        
        testAccount = new Account();
        testAccount.setId(accountId);
        testAccount.setName("Test Account");
        testAccount.setBalance(initialBalance);
        testAccount.setUser(testUser);
        testAccount.setEnable(true);
        testAccount.setDepositFee(0.05);

    }

    @Test
    @DisplayName("Deposit to account successfully")
    public void depositToAccountSuccess() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(transactionData.amount()).thenReturn(depositAmount);
        when(transactionGateway.create(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountGateway.update(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = depositAccountUseCase.execute(accountId, transactionData);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAccountBalance, result.getBalance().setScale(0, RoundingMode.HALF_UP));
        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, times(1)).update(any(Account.class));
    }

    @Test
    @DisplayName("Deposit to non-existing account should throw AccountNotFoundException")
    public void depositToNonExistingAccountShouldThrowException() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            depositAccountUseCase.execute(accountId, transactionData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, never()).verifyOwnershipOrAdmin(anyLong());
        verify(accountGateway, never()).update(any(Account.class));
    }

    @Test
    @DisplayName("Deposit to account without permission should throw AccessDeniedBusinessException")
    public void depositToAccountWithoutPermissionShouldThrowException() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            depositAccountUseCase.execute(accountId, transactionData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, never()).update(any(Account.class));
    }
}
