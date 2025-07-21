package com.finco.finco.goal.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.finco.finco.infrastructure.goal.dto.GoalUpdateData;
import com.finco.finco.usecase.goal.UpdateGoalUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Update Goal Test")
public class UpdateGoalUseCaseTest {

    @Mock
    private GoalGateway goalGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private UpdateGoalUseCase updateGoalUseCase;

    private User testUser;
    private Goal testGoal;
    private final Long goalId = 1L;
    private final Long userId = 1L;
    private GoalUpdateData updateData;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setEnable(true);

        testGoal = new Goal();
        testGoal.setId(goalId);
        testGoal.setName("Old Goal Name");
        testGoal.setDescription("Old Description");
        testGoal.setTargetAmount(BigDecimal.valueOf(1000));
        testGoal.setDeadLine(LocalDate.now().plusMonths(6));
        testGoal.setUser(testUser);
        testGoal.setEnable(true);

        updateData = new GoalUpdateData(
            "Updated Goal Name",
            BigDecimal.valueOf(2000),
            LocalDate.now().plusMonths(12),
            "Updated Description",
            false
        );
    }

    @Test
    @DisplayName("Update all goal fields successfully")
    public void updateAllGoalFieldsSuccess() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        when(goalGateway.update(any(Goal.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        Goal result = updateGoalUseCase.execute(goalId, updateData);

        // Assert
        assertNotNull(result);
        assertEquals(goalId, result.getId());
        assertEquals(updateData.name(), result.getName());
        assertEquals(updateData.description(), result.getDescription());
        assertEquals(updateData.targetAmount(), result.getTargetAmount());
        assertEquals(updateData.deadLine(), result.getDeadLine());
        assertEquals(updateData.enable(), result.isEnable());
        
        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
    }

    @Test
    @DisplayName("Update partial goal fields successfully")
    public void updatePartialGoalFieldsSuccess() {
        // Arrange
        GoalUpdateData partialUpdateData = new GoalUpdateData(
            "Updated Name Only",
            null,
            null,
            null,
            null
        );
        
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        when(goalGateway.update(any(Goal.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        Goal result = updateGoalUseCase.execute(goalId, partialUpdateData);

        // Assert
        assertNotNull(result);
        assertEquals(goalId, result.getId());
        assertEquals(partialUpdateData.name(), result.getName());
        assertEquals(testGoal.getDescription(), result.getDescription()); // Should remain unchanged
        assertEquals(testGoal.getTargetAmount(), result.getTargetAmount()); // Should remain unchanged
        assertEquals(testGoal.getDeadLine(), result.getDeadLine()); // Should remain unchanged
        assertTrue(result.isEnable()); // Should remain unchanged
        
        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(goalGateway, times(1)).update(any(Goal.class));
    }

    @Test
    @DisplayName("Update non-existent goal should throw AccessDeniedBusinessException")
    public void updateNonExistentGoalShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            updateGoalUseCase.execute(goalId, updateData);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, never()).verifyOwnershipOrAdmin(anyLong());
        verify(goalGateway, never()).update(any(Goal.class));
    }

    @Test
    @DisplayName("Update goal without ownership should throw AccessDeniedBusinessException")
    public void updateGoalWithoutOwnershipShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            updateGoalUseCase.execute(goalId, updateData);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(goalGateway, never()).update(any(Goal.class));
    }
}
