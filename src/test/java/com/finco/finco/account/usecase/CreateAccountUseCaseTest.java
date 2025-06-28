package com.finco.finco.account.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.account.CreateAccountUseCase;
import com.finco.finco.usecase.account.dto.IAccountRegistrationData;

@ExtendWith(MockitoExtension.class)
public class CreateAccountUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private AuthGateway authGateway;

    @Mock
    private IAccountRegistrationData accountData;

    private CreateAccountUseCase createAccountUseCase;
    private User testUser;
    private Account testAccount;
    private final Long userId = 1L;

    @BeforeEach
    public void setUp() {
        createAccountUseCase = new CreateAccountUseCase(accountGateway, userGateway, authGateway);

        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setAccounts(Collections.emptyList());

        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setUser(testUser);
        testAccount.setName("Test Account");
        testAccount.setBalance(BigDecimal.valueOf(1000));
        testAccount.setEnable(true);
        
    }

    @Test
    @DisplayName("Create account successfully")
    public void createAccountSuccess() {
        // Arrange
        when(accountData.userId()).thenReturn(userId);
        when(accountData.name()).thenReturn("Test Account");
        when(accountData.balance()).thenReturn(BigDecimal.valueOf(1000));
        when(accountData.currency()).thenReturn(CurrencyEnum.USD);
        when(accountData.description()).thenReturn("Test Description");
        
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(userGateway.findById(userId)).thenReturn(Optional.of(testUser));
        when(accountGateway.create(any(Account.class))).thenAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            account.setId(1L);
            return account;
        });

        // Act
        Account result = createAccountUseCase.execute(accountData);

        // Assert
        assertNotNull(result);
        assertEquals("Test Account", result.getName());
        assertEquals(BigDecimal.valueOf(1000), result.getBalance());
        assertEquals(CurrencyEnum.USD, result.getCurrency());
        assertTrue(result.isEnable());
        assertTrue(result.isDefault()); // First account should be default
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(1)).findById(userId);
        verify(accountGateway, times(1)).create(any(Account.class));
    }

    @Test
    @DisplayName("Create account for non-existing user should throw UserNotFoundException")
    public void createAccountForNonExistingUserShouldThrowException() {
        // Arrange
        when(accountData.userId()).thenReturn(userId);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(userGateway.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            createAccountUseCase.execute(accountData);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(1)).findById(userId);
        verify(accountGateway, never()).create(any(Account.class));
    }

    @Test
    @DisplayName("Create account without permission should throw AccessDeniedBusinessException")
    public void createAccountWithoutPermissionShouldThrowException() {
        // Arrange
        when(accountData.userId()).thenReturn(2L);
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(2L);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            createAccountUseCase.execute(accountData);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(2L);
        verify(userGateway, never()).findById(anyLong());
        verify(accountGateway, never()).create(any(Account.class));
    }
}
