package com.finco.finco.account.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.account.GetAllAccountUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get all accounts test")
public class GetAllAccountUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private GetAllAccountUseCase getAllAccountUseCase;
    private PageRequest pageRequest;
    private Account account1, account2;
    private PagedResult<Account> pagedResult;

    @BeforeEach
    public void setUp() {
        pageRequest = new PageRequest(0, 10, "name", "asc");

        User user1 = new User();
        user1.setId(1L);
        user1.setName("User One");

        account1 = new Account();
        account1.setId(1L);
        account1.setName("Account One");
        account1.setBalance(BigDecimal.valueOf(1000));
        account1.setUser(user1);

        account2 = new Account();
        account2.setId(2L);
        account2.setName("Account Two");
        account2.setBalance(BigDecimal.valueOf(2000));
        account2.setUser(user1);

        pagedResult = new PagedResult<>(List.of(account1, account2), 2, 1, 0, 10, true, false, false, false);
    }

    @Test
    @DisplayName("Get all accounts successfully with admin role")
    public void getAllAccountsSuccess() {
        // Arrange
        when(authGateway.isAuthenticatedUserInRole("ADMIN")).thenReturn(true);
        when(accountGateway.findAll(any(PageRequest.class))).thenReturn(pagedResult);

        // Act
        PagedResult<Account> result = getAllAccountUseCase.execute(pageRequest);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(authGateway, times(1)).isAuthenticatedUserInRole("ADMIN");
        verify(accountGateway, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Get all accounts without admin role should throw AccessDeniedBusinessException")
    public void getAllAccountsWithoutAdminRoleShouldThrowException() {
        // Arrange
        when(authGateway.isAuthenticatedUserInRole("ADMIN")).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getAllAccountUseCase.execute(pageRequest);
        });

        verify(authGateway, times(1)).isAuthenticatedUserInRole("ADMIN");
        verify(accountGateway, never()).findAll(any(PageRequest.class));
    }
}
