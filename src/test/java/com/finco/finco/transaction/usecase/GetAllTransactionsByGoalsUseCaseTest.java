package com.finco.finco.transaction.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.transaction.GetAllTransactionsByGoalsUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get All Transactions By Goals Test")
public class GetAllTransactionsByGoalsUseCaseTest {

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private GoalGateway goalGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private GetAllTransactionsByGoalsUseCase getAllTransactionsByGoalsUseCase;

    private User testUser;
    private Goal testGoal;
    private Transaction testTransaction;
    private final Long userId = 1L;
    private final Long goalId = 1L;
    private PageRequest pageRequest;
    private PagedResult<Transaction> pagedResult;

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

        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setUser(testUser);
        testTransaction.setGoal(testGoal);
        testTransaction.setAmount(BigDecimal.valueOf(100));
        testTransaction.setType(TransactionType.DEPOSIT_GOAL);
        testTransaction.setDate(LocalDateTime.now());

        pageRequest = new PageRequest(0, 10, "date", "DESC");
        pagedResult = new PagedResult<>(List.of(testTransaction), 1, 1, 0, 10, true, false, false, false);
    }

    @Test
    @DisplayName("Get all transactions by goal successfully")
    public void getAllTransactionsByGoalSuccess() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        when(transactionGateway.findAllByGoalId(goalId, pageRequest)).thenReturn(pagedResult);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        PagedResult<Transaction> result = getAllTransactionsByGoalsUseCase.execute(pageRequest, goalId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(testTransaction.getId(), result.getContent().get(0).getId());
        
        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, times(1)).findAllByGoalId(goalId, pageRequest);
    }

    @Test
    @DisplayName("Get empty transactions list when goal has no transactions")
    public void getEmptyTransactionsListWhenGoalHasNoTransactions() {
        // Arrange
        PagedResult<Transaction> emptyResult = new PagedResult<>(List.of(), 0, 0, 0, 10, true, false, false, false);
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        when(transactionGateway.findAllByGoalId(goalId, pageRequest)).thenReturn(emptyResult);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        PagedResult<Transaction> result = getAllTransactionsByGoalsUseCase.execute(pageRequest, goalId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        
        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, times(1)).findAllByGoalId(goalId, pageRequest);
    }

    @Test
    @DisplayName("Get transactions for non-existent goal should throw AccessDeniedBusinessException")
    public void getTransactionsForNonExistentGoalShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getAllTransactionsByGoalsUseCase.execute(pageRequest, goalId);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, never()).verifyOwnershipOrAdmin(anyLong());
        verify(transactionGateway, never()).findAllByGoalId(anyLong(), any());
    }

    @Test
    @DisplayName("Get transactions without permission should throw AccessDeniedBusinessException")
    public void getTransactionsWithoutPermissionShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getAllTransactionsByGoalsUseCase.execute(pageRequest, goalId);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, never()).findAllByGoalId(anyLong(), any());
    }
}
