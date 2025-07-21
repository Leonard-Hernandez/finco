package com.finco.finco.goal.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.goal.DeleteGoalUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Delete Goal Test")
public class DeleteGoalUseCaseTest {

    @Mock
    private GoalGateway goalGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private DeleteGoalUseCase deleteGoalUseCase;

    private User testUser;
    private Goal testGoal;
    private final Long goalId = 1L;
    private final Long userId = 1L;

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
    }

    @Test
    @DisplayName("Delete goal successfully")
    public void deleteGoalSuccess() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        when(goalGateway.delete(any(Goal.class))).thenAnswer(invocation -> {
            Goal goal = invocation.getArgument(0);
            goal.setEnable(false);
            return goal;
        });
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        Goal result = deleteGoalUseCase.execute(goalId);

        // Assert
        assertNotNull(result);
        assertEquals(goalId, result.getId());

        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(goalGateway, times(1)).delete(any(Goal.class));
    }

    @Test
    @DisplayName("Delete non-existent goal should throw AccessDeniedBusinessException")
    public void deleteNonExistentGoalShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            deleteGoalUseCase.execute(goalId);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, never()).verifyOwnershipOrAdmin(anyLong());
        verify(goalGateway, never()).delete(any(Goal.class));
    }

    @Test
    @DisplayName("Delete goal without ownership should throw AccessDeniedBusinessException")
    public void deleteGoalWithoutOwnershipShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            deleteGoalUseCase.execute(goalId);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(goalGateway, never()).delete(any(Goal.class));
    }
}
