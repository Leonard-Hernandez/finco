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
import com.finco.finco.entity.exception.InsufficientBalanceException;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.account.dto.AccountTransactionData;
import com.finco.finco.usecase.account.WithDrawAccountUseCase;

@ExtendWith(MockitoExtension.class)
public class WithDrawAccountUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private AuthGateway authGateway;

    @Mock
    private TransactionGateway transactionGateway;

    private WithDrawAccountUseCase withDrawAccountUseCase;
    private Account testAccount;
    private User testUser;
    private AccountTransactionData transactionData;
    private final Long accountId = 1L;
    private final Long userId = 1L;
    private final BigDecimal initialBalance = BigDecimal.valueOf(1000);
    private final BigDecimal withdrawAmount = BigDecimal.valueOf(500);
    private final BigDecimal expectedAccountBalance = BigDecimal.valueOf(500);
    private final BigDecimal excessiveWithdrawAmount = BigDecimal.valueOf(1500);

    @BeforeEach
    public void setUp() {
        withDrawAccountUseCase = new WithDrawAccountUseCase(accountGateway, authGateway, transactionGateway);

        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");

        testAccount = new Account();
        testAccount.setId(accountId);
        testAccount.setName("Test Account");
        testAccount.setBalance(initialBalance);
        testAccount.setUser(testUser);
        testAccount.setEnable(true);
        testAccount.setWithdrawFee(0.05);

        transactionData = new AccountTransactionData(withdrawAmount, "Withdraw", "Withdrawal", BigDecimal.valueOf(10));
    }

    @Test
    @DisplayName("Withdraw from account successfully")
    public void withdrawFromAccountSuccess() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(transactionGateway.create(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountGateway.update(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = withDrawAccountUseCase.execute(accountId, transactionData);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAccountBalance, result.getBalance().setScale(0, RoundingMode.HALF_UP));
        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, times(1)).update(any(Account.class));
    }

    @Test
    @DisplayName("Withdraw from non-existing account should throw AccessDeniedBusinessException")
    public void withdrawFromNonExistingAccountShouldThrowException() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            withDrawAccountUseCase.execute(accountId, transactionData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, never()).verifyOwnershipOrAdmin(anyLong());
        verify(accountGateway, never()).update(any(Account.class));
    }

    @Test
    @DisplayName("Withdraw from account without permission should throw AccessDeniedBusinessException")
    public void withdrawFromAccountWithoutPermissionShouldThrowException() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            withDrawAccountUseCase.execute(accountId, transactionData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, never()).update(any(Account.class));
    }

    @Test
    @DisplayName("Withdraw more than account balance should throw InsufficientFundsException")
    public void withdrawMoreThanBalanceShouldThrowException() {
        // Arrange
        transactionData = new AccountTransactionData(excessiveWithdrawAmount, "Withdraw", "Withdrawal", BigDecimal.valueOf(10));
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            withDrawAccountUseCase.execute(accountId, transactionData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, never()).update(any(Account.class));
    }
}
