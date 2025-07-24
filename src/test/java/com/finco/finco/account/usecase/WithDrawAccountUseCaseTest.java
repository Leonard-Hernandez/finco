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
import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.exception.InsufficientBalanceException;
import com.finco.finco.entity.goal.exception.BalanceInGoalException;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
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

    @InjectMocks
    private WithDrawAccountUseCase withDrawAccountUseCase;
    
    private User testUser;
    private Account testAccount;
    private AccountTransactionData transactionData;
    private final Long accountId = 1L;
    private final Long userId = 1L;
    private final BigDecimal initialBalance = BigDecimal.valueOf(1000);
    private final BigDecimal withdrawAmount = BigDecimal.valueOf(500);
    private final BigDecimal expectedAccountBalance = BigDecimal.valueOf(475);

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
        testAccount.setWithdrawFee(0.05);
        testAccount.setType(AccountType.CASH);

        transactionData = new AccountTransactionData(withdrawAmount, "Withdraw", "Withdrawal");
    }

    @Test
    @DisplayName("Withdraw from account successfully")
    public void withdrawFromAccountSuccess() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(accountGateway.update(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountGateway.getTotalBalanceInGoalsByAccount(accountId)).thenReturn(BigDecimal.ZERO);

        // Act
        Account result = withDrawAccountUseCase.execute(accountId, transactionData);

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
        assertEquals(accountId, transaction.getAccount().getId());
        assertEquals(userId, transaction.getUser().getId());
        assertEquals(withdrawAmount, transaction.getAmount().setScale(0, RoundingMode.HALF_UP));
        assertEquals(TransactionType.WITHDRAW, transaction.getType());
        assertEquals("Withdraw", transaction.getCategory());
        assertEquals("Withdrawal", transaction.getDescription());
        assertEquals(userId, transaction.getUser().getId());
        assertEquals(BigDecimal.valueOf(25), transaction.getFee().setScale(0, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("Withdraw from credit account successfully")
    public void withdrawFromCreditAccountSuccess() {
        // Arrange
        testAccount.setType(AccountType.CREDIT);
        testAccount.setBalance(BigDecimal.ZERO);
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(accountGateway.update(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = withDrawAccountUseCase.execute(accountId, transactionData);

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal(-525), result.getBalance().setScale(0, RoundingMode.HALF_UP));
        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, times(1)).update(any(Account.class));

        // Assert transaction
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionGateway, times(1)).create(transactionCaptor.capture());

        Transaction transaction = transactionCaptor.getValue();
        assertNotNull(transaction);
        assertEquals(accountId, transaction.getAccount().getId());
        assertEquals(userId, transaction.getUser().getId());
        assertEquals(new BigDecimal(500), transaction.getAmount().setScale(0, RoundingMode.HALF_UP));
        assertEquals(TransactionType.WITHDRAW, transaction.getType());
        assertEquals("Withdraw", transaction.getCategory());
        assertEquals("Withdrawal", transaction.getDescription());
        assertEquals(userId, transaction.getUser().getId());
        assertEquals(BigDecimal.valueOf(25), transaction.getFee().setScale(0, RoundingMode.HALF_UP));
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
        transactionData = new AccountTransactionData(BigDecimal.valueOf(1001), "Withdraw", "Withdrawal");
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(accountGateway.getTotalBalanceInGoalsByAccount(accountId)).thenReturn(BigDecimal.ZERO);

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            withDrawAccountUseCase.execute(accountId, transactionData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, never()).update(any(Account.class));
    }

    @Test
    @DisplayName("Withdraw account in goals should throw BalanceInGoalException")
    public void withdrawAccountInGoalsShouldThrowException() {
        // Arrange
        transactionData = new AccountTransactionData(BigDecimal.valueOf(501), "Withdraw", "Withdrawal");
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(accountGateway.getTotalBalanceInGoalsByAccount(accountId)).thenReturn(BigDecimal.valueOf(500));

        // Act & Assert
        assertThrows(BalanceInGoalException.class, () -> {
            withDrawAccountUseCase.execute(accountId, transactionData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, never()).update(any(Account.class));
    }
}
