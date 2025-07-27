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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.account.dto.AccountRegistrationData;
import com.finco.finco.usecase.account.CreateAccountUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Create account test")
public class CreateAccountUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private CreateAccountUseCase createAccountUseCase;
    
    private User testUser;
    private Account testAccount;
    private AccountRegistrationData accountData;
    private final Long userId = 1L;

    @BeforeEach
    public void setUp() {
        accountData = new AccountRegistrationData(
                "Test Account",
                AccountType.CHECKING,
                BigDecimal.valueOf(1000),
                CurrencyEnum.USD,
                "Test Description",
                0.0,
                0.0);

        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setAccounts(Collections.emptyList());

        testAccount = new Account();
        testAccount.setId(userId);
        testAccount.setUser(testUser);
        testAccount.setName("Test Account");
        testAccount.setBalance(BigDecimal.valueOf(1000));
        testAccount.setEnable(true);

    }

    @Test
    @DisplayName("Create account successfully")
    public void createAccountSuccess() {
        // Arrange
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(userGateway.findById(userId)).thenReturn(Optional.of(testUser));
        when(accountGateway.create(any(Account.class))).thenAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            account.setId(1L);
            return account;
        });

        // Act
        Account result = createAccountUseCase.execute(userId, accountData);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals("Test Account", result.getName());
        assertEquals(BigDecimal.valueOf(1000), result.getBalance());
        assertEquals(CurrencyEnum.USD, result.getCurrency());
        assertTrue(result.isEnable());
        assertTrue(result.isDefault()); // First account should be default
        assertEquals(0.0, result.getWithdrawFee());
        assertEquals(0.0, result.getDepositFee());

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(1)).findById(userId);
        verify(accountGateway, times(1)).create(any(Account.class));
    }

    @Test
    @DisplayName("Create account for non-existing user should throw UserNotFoundException")
    public void createAccountForNonExistingUserShouldThrowException() {
        // Arrange
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(userGateway.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            createAccountUseCase.execute(userId, accountData);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(1)).findById(userId);
        verify(accountGateway, never()).create(any(Account.class));
    }

    @Test
    @DisplayName("Create account without permission should throw AccessDeniedBusinessException")
    public void createAccountWithoutPermissionShouldThrowException() {
        // Arrange
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(2L);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            createAccountUseCase.execute(2L, accountData);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(2L);
        verify(userGateway, never()).findById(anyLong());
        verify(accountGateway, never()).create(any(Account.class));
    }
}
