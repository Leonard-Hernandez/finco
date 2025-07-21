package com.finco.finco.goal.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
import com.finco.finco.entity.exception.InsufficientBalanceException;
import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.goalAccountBalance.gateway.GoalAccountBalanceGateway;
import com.finco.finco.entity.goalAccountBalance.model.GoalAccountBalance;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.goal.dto.GoalTransactionData;
import com.finco.finco.usecase.goal.DepositGoalUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Deposit to Goal Test")
public class DepositGoalUseCaseTest {

    @Mock
    private GoalGateway goalGateway;

    @Mock
    private GoalAccountBalanceGateway goalAccountBalanceGateway;

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private DepositGoalUseCase depositGoalUseCase;

    private User testUser;
    private Goal testGoal;
    private Account testAccount;
    private GoalAccountBalance testGoalAccountBalance;
    private final Long goalId = 1L;
    private final Long accountId = 1L;
    private final Long userId = 1L;
    private final BigDecimal depositAmount = BigDecimal.valueOf(500);
    private GoalTransactionData transactionData;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setEnable(true);

        testGoal = new Goal();
        testGoal.setId(goalId);
        testGoal.setName("Test Goal");
        testGoal.setTargetAmount(BigDecimal.valueOf(1000));
        testGoal.setDeadLine(LocalDate.now().plusMonths(6));
        testGoal.setUser(testUser);
        testGoal.setEnable(true);

        testAccount = new Account();
        testAccount.setId(accountId);
        testAccount.setBalance(BigDecimal.valueOf(2000));
        testAccount.setUser(testUser);

        testGoalAccountBalance = new GoalAccountBalance();
        testGoalAccountBalance.setId(1L);
        testGoalAccountBalance.setGoal(testGoal);
        testGoalAccountBalance.setAccount(testAccount);
        testGoalAccountBalance.setBalance(BigDecimal.valueOf(500));

        transactionData = new GoalTransactionData(
            accountId,
            depositAmount,
            "Savings",
            "Monthly savings deposit"
        );
    }

    @Test
    @DisplayName("Deposit to goal successfully with new goal account balance")
    public void depositToGoalSuccessWithNewBalance() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(goalAccountBalanceGateway.findAllByGoalId(goalId)).thenReturn(List.of());
        when(goalAccountBalanceGateway.getTotalBalanceInGoalsByAccount(accountId)).thenReturn(BigDecimal.ZERO);
        when(goalAccountBalanceGateway.create(any())).thenAnswer(invocation -> {
            GoalAccountBalance gab = invocation.getArgument(0);
            gab.setId(1L);
            return gab;
        });
        when(transactionGateway.create(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(goalGateway.update(any())).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        Goal result = depositGoalUseCase.execute(goalId, transactionData);

        // Assert
        assertNotNull(result);
        verify(goalGateway, times(1)).findById(goalId);
        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(2)).verifyOwnershipOrAdmin(userId);
        verify(goalAccountBalanceGateway, times(1)).create(any());
        verify(transactionGateway, times(1)).create(any());
        verify(goalGateway, times(1)).update(any());

        // transaction assert
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionGateway, times(1)).create(transactionCaptor.capture());
        Transaction transaction = transactionCaptor.getValue();
        assertEquals(transactionData.accountId(), transaction.getAccount().getId());
        assertEquals(goalId, transaction.getGoal().getId());
        assertEquals(userId, transaction.getUser().getId());
        assertEquals(TransactionType.DEPOSIT_GOAL, transaction.getType());
        assertEquals(transactionData.amount(), transaction.getAmount());
        assertEquals(transactionData.category(), transaction.getCategory());
        assertEquals(transactionData.description(), transaction.getDescription());
    }

    @Test
    @DisplayName("Deposit to goal successfully with existing goal account balance")
    public void depositToGoalSuccessWithExistingBalance() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(goalAccountBalanceGateway.findAllByGoalId(goalId)).thenReturn(List.of(testGoalAccountBalance));
        when(goalAccountBalanceGateway.getTotalBalanceInGoalsByAccount(accountId)).thenReturn(BigDecimal.ZERO);
        when(goalAccountBalanceGateway.update(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(goalGateway.update(any())).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        Goal result = depositGoalUseCase.execute(goalId, transactionData);

        // Assert
        assertNotNull(result);
        assertEquals(goalId, result.getId());
        assertEquals(BigDecimal.valueOf(1000), testGoalAccountBalance.getBalance());
        verify(goalAccountBalanceGateway, times(1)).update(any());
        verify(goalAccountBalanceGateway, never()).create(any());

        // transaction assert
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionGateway, times(1)).create(transactionCaptor.capture());
        Transaction transaction = transactionCaptor.getValue();
        assertEquals(transactionData.accountId(), transaction.getAccount().getId());
        assertEquals(goalId, transaction.getGoal().getId());
        assertEquals(userId, transaction.getUser().getId());
        assertEquals(TransactionType.DEPOSIT_GOAL, transaction.getType());
        assertEquals(transactionData.amount(), transaction.getAmount());
        assertEquals(transactionData.category(), transaction.getCategory());
        assertEquals(transactionData.description(), transaction.getDescription());
    }

    @Test
    @DisplayName("Deposit to non-existent goal should throw AccessDeniedBusinessException")
    public void depositToNonExistentGoalShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            depositGoalUseCase.execute(goalId, transactionData);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(accountGateway, never()).findById(any());
        verify(goalAccountBalanceGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deposit with insufficient balance should throw InsufficientBalanceException")
    public void depositWithInsufficientBalanceShouldThrowException() {
        // Arrange
        testAccount.setBalance(BigDecimal.valueOf(499));
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        when(goalAccountBalanceGateway.getTotalBalanceInGoalsByAccount(accountId)).thenReturn(BigDecimal.ZERO);
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            depositGoalUseCase.execute(goalId, transactionData);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(accountGateway, times(1)).findById(accountId);
        verify(goalAccountBalanceGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deposit with insufficient balance because goal should throw InsufficientBalanceException")
    public void depositWithInsufficientBalanceInGoalShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        when(goalAccountBalanceGateway.getTotalBalanceInGoalsByAccount(accountId)).thenReturn(testAccount.getBalance());
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            depositGoalUseCase.execute(goalId, transactionData);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(accountGateway, times(1)).findById(accountId);
        verify(goalAccountBalanceGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deposit without ownership should throw AccessDeniedBusinessException")
    public void depositWithoutOwnershipShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            depositGoalUseCase.execute(goalId, transactionData);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(accountGateway, never()).findById(any());
        verify(goalAccountBalanceGateway, never()).create(any());
    }
}
