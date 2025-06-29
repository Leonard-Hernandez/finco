package com.finco.finco.account.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.entity.exception.IncompatibleCurrencyException;
import com.finco.finco.entity.exception.InsufficientBalanceException;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.account.TransferAccountUseCase;
import com.finco.finco.usecase.account.dto.IAccountTransferData;

@ExtendWith(MockitoExtension.class)
public class TransferAccountUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private AuthGateway authGateway;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private IAccountTransferData transferData;

    private TransferAccountUseCase transferAccountUseCase;
    private Account testAccount;
    private Account testAccount2;
    private User testUser;
    private final Long accountId = 1L;
    private final Long transferAccountId = 2L;
    private final Long userId = 1L;
    private final BigDecimal initialBalance = BigDecimal.valueOf(1000);
    private final BigDecimal transferAmount = BigDecimal.valueOf(500);

    @BeforeEach
    public void setUp() {
        transferAccountUseCase = new TransferAccountUseCase(accountGateway, authGateway, transactionGateway);
        
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        
        testAccount = new Account();
        testAccount.setId(accountId);
        testAccount.setName("Test Account");
        testAccount.setBalance(initialBalance);
        testAccount.setUser(testUser);
        testAccount.setEnable(true);

        testAccount2 = new Account();
        testAccount2.setId(transferAccountId);
        testAccount2.setName("Test Account");
        testAccount2.setBalance(initialBalance);
        testAccount2.setUser(testUser);
        testAccount2.setEnable(true);
    
    }

    @Test
    @DisplayName("Transfer to account successfully")
    public void transferToAccountSuccess() {
        // Arrange
        when(transferData.amount()).thenReturn(transferAmount);
        when(transferData.transferAccountId()).thenReturn(transferAccountId);
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(accountGateway.findById(transferAccountId)).thenReturn(Optional.of(testAccount2));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(transactionGateway.create(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountGateway.update(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = transferAccountUseCase.execute(accountId, transferData);

        // Assert
        assertNotNull(result);
        assertEquals(initialBalance.subtract(transferAmount), result.getBalance());
        assertEquals(initialBalance.add(transferAmount), testAccount2.getBalance());
        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(2)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, times(2)).update(any(Account.class));
    }

    @Test
    @DisplayName("Transfer to non-existing account should throw AccessDeniedBusinessException")
    public void transferToNonExistingAccountShouldThrowException() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            transferAccountUseCase.execute(accountId, transferData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, never()).verifyOwnershipOrAdmin(anyLong());
        verify(accountGateway, never()).update(any(Account.class));
    }

    @Test
    @DisplayName("Transfer to account with insufficient balance should throw InsufficientBalanceException")
    public void transferToAccountWithInsufficientBalanceShouldThrowException() {
        // Arrange
        when(transferData.amount()).thenReturn(BigDecimal.valueOf(1500));
        when(transferData.transferAccountId()).thenReturn(transferAccountId);
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(accountGateway.findById(transferAccountId)).thenReturn(Optional.of(testAccount2));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            transferAccountUseCase.execute(accountId, transferData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(2)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, never()).update(any(Account.class));
    }

    @Test
    @DisplayName("Transfer to account with incompatible currency should throw IncompatibleCurrencyException")
    public void transferToAccountWithIncompatibleCurrencyShouldThrowException() {
        // Arrange
        testAccount.setCurrency(CurrencyEnum.USD);
        when(transferData.transferAccountId()).thenReturn(transferAccountId);
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(accountGateway.findById(transferAccountId)).thenReturn(Optional.of(testAccount2));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(IncompatibleCurrencyException.class, () -> {
            transferAccountUseCase.execute(accountId, transferData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(2)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, never()).update(any(Account.class));
    }

}
