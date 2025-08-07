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
import com.finco.finco.entity.pagination.filter.IGoalFilterData;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.goal.dto.GoalFilterData;
import com.finco.finco.usecase.goal.GetAllGoalsUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get All Goals Test")
public class GetAllGoalsUseCaseTest {

    @Mock
    private GoalGateway goalGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private GetAllGoalsUseCase getAllGoalsUseCase;

    private PageRequest pageRequest;
    private PagedResult<Goal> pagedResult;
    private Goal testGoal;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test Admin");
        testUser.setEnable(true);

        pageRequest = new PageRequest(0, 10, "name", "ASC");
        
        testGoal = new Goal();
        testGoal.setId(1L);
        testGoal.setName("Test Goal");
        testGoal.setTargetAmount(BigDecimal.valueOf(1000));
        testGoal.setDeadLine(LocalDate.now().plusMonths(6));
        testGoal.setUser(testUser);
        testGoal.setEnable(true);
        
        pagedResult = new PagedResult<>(
            List.of(testGoal), 
            1, 
            1, 
            0, 
            10, 
            true, 
            false, 
            false, 
            false
        );
    }

    @Test
    @DisplayName("Get all goals successfully when user is admin")
    void getAllGoalsSuccess() {
        // Arrange
        IGoalFilterData filterData = new GoalFilterData(null, null);
        when(authGateway.isAuthenticatedUserInRole("ADMIN")).thenReturn(true);
        when(goalGateway.findAll(pageRequest)).thenReturn(pagedResult);

        // Act
        PagedResult<Goal> result = getAllGoalsUseCase.execute(pageRequest, filterData);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("Test Goal", result.getContent().get(0).getName());
        
        verify(authGateway, times(1)).isAuthenticatedUserInRole("ADMIN");
        verify(goalGateway, times(1)).findAll(pageRequest);
    }

    @Test
    @DisplayName("Throw AccessDeniedBusinessException when user is not admin")
    void throwExceptionWhenUserIsNotAdmin() {
        // Arrange
        IGoalFilterData filterData = new GoalFilterData(null, null);
        when(authGateway.isAuthenticatedUserInRole("ADMIN")).thenReturn(false);

        // Act & Assert
        AccessDeniedBusinessException exception = assertThrows(
            AccessDeniedBusinessException.class, 
            () -> getAllGoalsUseCase.execute(pageRequest, filterData)
        );
        
        assertNotNull(exception);
        verify(authGateway, times(1)).isAuthenticatedUserInRole("ADMIN");
        verify(goalGateway, never()).findAll(any(PageRequest.class));
    }
      
}
