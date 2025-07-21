package com.finco.finco.goal.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.goal.GetAllGoalsByUserUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get All Goals By User Test")
public class GetAllGoalsByUserUseCaseTest {

    @Mock
    private GoalGateway goalGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private GetAllGoalsByUserUseCase getAllGoalsByUserUseCase;

    private User testUser;
    private final Long userId = 1L;
    private PageRequest pageRequest;
    private PagedResult<Goal> pagedResult;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setEnable(true);

        pageRequest = new PageRequest(0, 10, "name", "ASC");
        pagedResult = new PagedResult<>(List.of(createTestGoal()), 1, 1, 0, 10, true, false, false, false);
    }

    private Goal createTestGoal() {
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setName("Test Goal");
        goal.setTargetAmount(BigDecimal.valueOf(1000));
        goal.setDeadLine(LocalDate.now().plusMonths(6));
        goal.setUser(testUser);
        goal.setEnable(true);
        return goal;
    }

    @Test
    @DisplayName("Get all goals by user successfully")
    public void getAllGoalsByUserSuccess() {
        // Arrange
        when(goalGateway.findAllByUserId(userId, pageRequest)).thenReturn(pagedResult);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        PagedResult<Goal> result = getAllGoalsByUserUseCase.execute(userId, pageRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("Test Goal", result.getContent().get(0).getName());
        
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(goalGateway, times(1)).findAllByUserId(userId, pageRequest);
    }

    @Test
    @DisplayName("Get empty goals list when user has no goals")
    public void getEmptyGoalsListWhenUserHasNoGoals() {
        // Arrange
        PagedResult<Goal> emptyResult = new PagedResult<>(List.of(), 0, 0, 0, 10, true, false, false, false);
        when(goalGateway.findAllByUserId(userId, pageRequest)).thenReturn(emptyResult);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        PagedResult<Goal> result = getAllGoalsByUserUseCase.execute(userId, pageRequest);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(goalGateway, times(1)).findAllByUserId(userId, pageRequest);
    }

    @Test
    @DisplayName("Get goals without permission should throw AccessDeniedBusinessException")
    public void getGoalsWithoutPermissionShouldThrowException() {
        // Arrange
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getAllGoalsByUserUseCase.execute(userId, pageRequest);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(goalGateway, never()).findAllByUserId(any(), any());
    }

    @Test
    @DisplayName("Get goals with pagination")
    public void getGoalsWithPagination() {
        // Arrange
        PageRequest customPageRequest = new PageRequest(1, 5, "name", "ASC");
        when(goalGateway.findAllByUserId(userId, customPageRequest)).thenReturn(pagedResult);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        PagedResult<Goal> result = getAllGoalsByUserUseCase.execute(userId, customPageRequest);

        // Assert
        assertNotNull(result);
        verify(goalGateway, times(1)).findAllByUserId(userId, customPageRequest);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
    }
}
