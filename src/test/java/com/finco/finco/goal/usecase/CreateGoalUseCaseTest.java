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
import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.goal.dto.GoalRegistrationData;
import com.finco.finco.usecase.goal.CreateGoalUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Create Goal Test")
public class CreateGoalUseCaseTest {

    @Mock
    private GoalGateway goalGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private CreateGoalUseCase createGoalUseCase;

    private User testUser;
    private Goal testGoal;
    private final Long userId = 1L;
    private GoalRegistrationData goalData;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setEnable(true);

        goalData = new GoalRegistrationData(
            "Test Goal",
            BigDecimal.valueOf(1000),
            LocalDate.now().plusMonths(6),
            "Test Description"
        );

        testGoal = new Goal();
        testGoal.setId(1L);
        testGoal.setName(goalData.name());
        testGoal.setDescription(goalData.description());
        testGoal.setTargetAmount(goalData.targetAmount());
        testGoal.setDeadLine(goalData.deadLine());
        testGoal.setUser(testUser);
    }

    @Test
    @DisplayName("Create goal successfully")
    public void createGoalSuccess() {
        // Arrange
        when(userGateway.findById(userId)).thenReturn(Optional.of(testUser));
        when(goalGateway.create(any(Goal.class))).thenAnswer(invocation -> {
            Goal goal = invocation.getArgument(0);
            goal.setId(1L);
            return goal;
        });
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        Goal result = createGoalUseCase.execute(userId, goalData);

        // Assert
        assertNotNull(result);
        assertEquals(goalData.name(), result.getName());
        assertEquals(goalData.description(), result.getDescription());
        assertEquals(goalData.targetAmount(), result.getTargetAmount());
        assertEquals(goalData.deadLine(), result.getDeadLine());
        assertTrue(result.isEnable());
        
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(1)).findById(userId);
        verify(goalGateway, times(1)).create(any(Goal.class));
    }

    @Test
    @DisplayName("Create goal with non-existent user should throw UserNotFoundException")
    public void createGoalWithNonExistentUserShouldThrowException() {
        // Arrange
        when(userGateway.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            createGoalUseCase.execute(userId, goalData);
        });

        verify(userGateway, times(1)).findById(userId);
        verify(goalGateway, never()).create(any(Goal.class));
    }

    @Test
    @DisplayName("Create goal without ownership should throw AccessDeniedBusinessException")
    public void createGoalWithoutOwnershipShouldThrowException() {
        // Arrange
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            createGoalUseCase.execute(userId, goalData);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, never()).findById(userId);
        verify(goalGateway, never()).create(any(Goal.class));
    }
}
