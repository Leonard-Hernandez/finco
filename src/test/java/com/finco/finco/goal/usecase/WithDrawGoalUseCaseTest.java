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
import com.finco.finco.entity.goal.exception.AccountNotAssociatedWithGoalException;
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
import com.finco.finco.usecase.goal.WithDrawGoalUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Withdraw from Goal Test")
public class WithDrawGoalUseCaseTest {

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
    private WithDrawGoalUseCase withDrawGoalUseCase;

    private User testUser;
    private Goal testGoal;
    private Account testAccount;
    private GoalAccountBalance testGoalAccountBalance;
    private final Long goalId = 1L;
    private final Long accountId = 1L;
    private final Long userId = 1L;
    private final BigDecimal withdrawAmount = BigDecimal.valueOf(300);
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
        testGoalAccountBalance.setBalance(BigDecimal.valueOf(500)); // Initial balance

        transactionData = new GoalTransactionData(
            accountId,
            withdrawAmount,
            "Emergency withdrawal",
            "Emergency"
        );
    }

    @Test
    @DisplayName("Withdraw from goal successfully")
    public void withdrawFromGoalSuccess() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(goalAccountBalanceGateway.findAllByGoalId(goalId))
            .thenReturn(List.of(testGoalAccountBalance));
        when(goalGateway.update(any())).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        Goal result = withDrawGoalUseCase.execute(goalId, transactionData);

        // Assert
        assertNotNull(result);
        verify(goalGateway, times(1)).findById(goalId);
        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(2)).verifyOwnershipOrAdmin(userId);
        verify(goalAccountBalanceGateway, times(1)).update(any());
        verify(transactionGateway, times(1)).create(any());
        verify(goalGateway, times(1)).update(any());

        // transaction assert
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionGateway, times(1)).create(transactionCaptor.capture());

        Transaction transaction = transactionCaptor.getValue();
        assertEquals(accountId, transaction.getAccount().getId());
        assertEquals(goalId, transaction.getGoal().getId());
        assertEquals(userId, transaction.getUser().getId());
        assertEquals(TransactionType.WITHDRAW_GOAL, transaction.getType());
        assertEquals(withdrawAmount, transaction.getAmount());
        assertEquals("Emergency withdrawal", transaction.getDescription());
        assertEquals("Emergency", transaction.getCategory());

    }

    @Test
    @DisplayName("Withdraw from non-existent goal should throw AccessDeniedBusinessException")
    public void withdrawFromNonExistentGoalShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            withDrawGoalUseCase.execute(goalId, transactionData);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(accountGateway, never()).findById(any());
        verify(goalAccountBalanceGateway, never()).update(any());
    }

    @Test
    @DisplayName("Withdraw with insufficient balance should throw InsufficientBalanceException")
    public void withdrawWithInsufficientBalanceShouldThrowException() {
        // Arrange
        transactionData = new GoalTransactionData(
            accountId,
            BigDecimal.valueOf(1000), // More than the current balance of 500
            "Emergency",
            "Emergency withdrawal"
        );
        
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(goalAccountBalanceGateway.findAllByGoalId(goalId))
            .thenReturn(List.of(testGoalAccountBalance));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            withDrawGoalUseCase.execute(goalId, transactionData);
        });

        verify(goalAccountBalanceGateway, never()).update(any());
    }

    @Test
    @DisplayName("Withdraw from account not associated with goal should throw AccountNotAssociatedWithGoalException")
    public void withdrawFromNonAssociatedAccountShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(goalAccountBalanceGateway.findAllByGoalId(goalId)).thenReturn(List.of());
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccountNotAssociatedWithGoalException.class, () -> {
            withDrawGoalUseCase.execute(goalId, transactionData);
        });

        verify(goalAccountBalanceGateway, never()).update(any());
    }

    @Test
    @DisplayName("Withdraw without ownership should throw AccessDeniedBusinessException")
    public void withdrawWithoutOwnershipShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            withDrawGoalUseCase.execute(goalId, transactionData);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(accountGateway, never()).findById(any());
        verify(goalAccountBalanceGateway, never()).update(any());
    }
}
