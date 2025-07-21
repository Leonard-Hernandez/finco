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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.exception.AmountMustBeGreaterThanZeroException;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.account.dto.AccountTransactionData;
import com.finco.finco.usecase.account.DepositAccountUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Deposit account test")
public class DepositAccountUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private AuthGateway authGateway;

    @Mock
    private TransactionGateway transactionGateway;

    @InjectMocks
    private DepositAccountUseCase depositAccountUseCase;

    private Account testAccount;
    private User testUser;
    private final Long accountId = 1L;
    private final Long userId = 1L;
    private final BigDecimal initialBalance = BigDecimal.valueOf(1000);
    private final BigDecimal depositAmount = BigDecimal.valueOf(500);
    private BigDecimal expectedAccountBalance = BigDecimal.valueOf(1475);
    private AccountTransactionData transactionData;

    @BeforeEach
    public void setUp() {
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

        transactionData = new AccountTransactionData(depositAmount, "Deposit", "Deposit");
    }

    @Test
    @DisplayName("Deposit to account successfully")
    public void depositToAccountSuccess() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(accountGateway.update(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = depositAccountUseCase.execute(accountId, transactionData);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAccountBalance, result.getBalance().setScale(0, RoundingMode.HALF_UP));
        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, times(1)).update(any(Account.class));

        // Assert transaction
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionGateway, times(1)).create(transactionCaptor.capture());

        Transaction transaction = transactionCaptor.getValue();
        assertNotNull(transaction);
        assertEquals(BigDecimal.valueOf(475), transaction.getAmount().setScale(0, RoundingMode.HALF_UP));
        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        assertEquals("Deposit", transaction.getDescription());
        assertEquals("Deposit", transaction.getCategory());
        assertEquals(accountId, transaction.getAccount().getId());
        assertEquals(userId, transaction.getUser().getId());
        assertEquals(BigDecimal.valueOf(25), transaction.getFee().setScale(0, RoundingMode.HALF_UP));

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

    @Test
    @DisplayName("Deposit to account with invalid amount should throw IllegalArgumentException")
    public void depositToAccountWithInvalidAmountShouldThrowException() {
        // Arrange
        transactionData = new AccountTransactionData(BigDecimal.valueOf(-1), "Deposit", "Deposit");
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));

        // Act & Assert
        assertThrows(AmountMustBeGreaterThanZeroException.class, () -> {
            depositAccountUseCase.execute(accountId, transactionData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(anyLong());
        verify(accountGateway, never()).update(any(Account.class));
    }
}
