package com.finco.finco.goal.usecase;

import static org.junit.jupiter.api.Assertions.*;
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
import com.finco.finco.usecase.goal.GetGoalUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get Goal Test")
public class GetGoalUseCaseTest {

    @Mock
    private GoalGateway goalGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private GetGoalUseCase getGoalUseCase;

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
        testGoal.setDescription("Test Description");
        testGoal.setTargetAmount(BigDecimal.valueOf(1000));
        testGoal.setDeadLine(LocalDate.now().plusMonths(6));
        testGoal.setUser(testUser);
        testGoal.setEnable(true);
    }

    @Test
    @DisplayName("Get goal successfully")
    public void getGoalSuccess() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        Goal result = getGoalUseCase.execute(goalId);

        // Assert
        assertNotNull(result);
        assertEquals(goalId, result.getId());
        assertEquals("Test Goal", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(BigDecimal.valueOf(1000), result.getTargetAmount());
        assertEquals(testUser, result.getUser());
        assertTrue(result.isEnable());
        
        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
    }

    @Test
    @DisplayName("Get non-existent goal should throw AccessDeniedBusinessException")
    public void getNonExistentGoalShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getGoalUseCase.execute(goalId);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, never()).verifyOwnershipOrAdmin(anyLong());
    }

    @Test
    @DisplayName("Get goal without ownership should throw AccessDeniedBusinessException")
    public void getGoalWithoutOwnershipShouldThrowException() {
        // Arrange
        when(goalGateway.findById(goalId)).thenReturn(Optional.of(testGoal));
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getGoalUseCase.execute(goalId);
        });

        verify(goalGateway, times(1)).findById(goalId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
    }
}
