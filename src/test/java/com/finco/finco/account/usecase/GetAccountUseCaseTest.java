package com.finco.finco.account.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.account.GetAccountUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get account test")
public class GetAccountUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private GetAccountUseCase getAccountUseCase;

    private Account testAccount;
    private User testUser;
    private final Long accountId = 1L;
    private final Long userId = 1L;

    @BeforeEach
    public void setUp() {

        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        testAccount = new Account();
        testAccount.setId(accountId);
        testAccount.setName("Test Account");
        testAccount.setBalance(BigDecimal.valueOf(1000));
        testAccount.setUser(testUser);
        testAccount.setEnable(true);
        testAccount.setDefault(true);
        testAccount.setWithdrawFee(0.0);
        testAccount.setDepositFee(0.0);
        testAccount.setCurrency(CurrencyEnum.USD);
    }

    @Test
    @DisplayName("Get existing account successfully")
    public void getExistingAccountSuccess() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        Account result = getAccountUseCase.execute(accountId);

        // Assert
        assertNotNull(result);
        assertEquals(accountId, result.getId());
        assertEquals("Test Account", result.getName());
        assertEquals(userId, result.getUser().getId());
        assertEquals(true, result.isEnable());
        assertEquals(true, result.isDefault());
        assertEquals(0.0, result.getWithdrawFee());
        assertEquals(0.0, result.getDepositFee());
        assertEquals(CurrencyEnum.USD, result.getCurrency());

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
    }

    @Test
    @DisplayName("Get non-existing account should throw AccessDeniedBusinessException")
    public void getNonExistingAccountShouldThrowException() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getAccountUseCase.execute(accountId);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, never()).verifyOwnershipOrAdmin(anyLong());
    }

    @Test
    @DisplayName("Get account without permission should throw AccessDeniedBusinessException")
    public void getAccountWithoutPermissionShouldThrowException() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getAccountUseCase.execute(accountId);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
    }
}
