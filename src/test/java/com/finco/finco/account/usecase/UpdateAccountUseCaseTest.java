package com.finco.finco.account.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.account.exception.CannotDeactivateDefaultAccountException;
import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.account.UpdateAccountUseCase;
import com.finco.finco.usecase.account.dto.IAccountUpdateData;

@ExtendWith(MockitoExtension.class)
public class UpdateAccountUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private AuthGateway authGateway;

    @Mock
    private IAccountUpdateData updateData;

    private UpdateAccountUseCase updateAccountUseCase;
    private Account testAccount;
    private Account defaultAccount;
    private User testUser;
    private final Long accountId = 1L;
    private final Long userId = 1L;

    @BeforeEach
    public void setUp() {
        updateAccountUseCase = new UpdateAccountUseCase(accountGateway, authGateway);
        
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        
        testAccount = new Account();
        testAccount.setId(accountId);
        testAccount.setName("Original Name");
        testAccount.setBalance(BigDecimal.valueOf(1000));
        testAccount.setUser(testUser);
        testAccount.setEnable(true);
        testAccount.setDefault(false);

        defaultAccount = new Account();
        defaultAccount.setId(accountId);
        defaultAccount.setName("Default Name");
        defaultAccount.setBalance(BigDecimal.valueOf(1000));
        defaultAccount.setUser(testUser);
        defaultAccount.setEnable(true);
        defaultAccount.setDefault(true);
    }

    @Test
    @DisplayName("Update account name successfully")
    public void updateAccountNameSuccess() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(updateData.name()).thenReturn("Updated Name");
        when(updateData.enable()).thenReturn(null);
        when(updateData.isDefault()).thenReturn(null);
        when(accountGateway.update(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = updateAccountUseCase.execute(accountId, updateData);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertTrue(result.isEnable());
        assertFalse(result.isDefault());
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, times(1)).findById(accountId);
        verify(accountGateway, times(1)).update(any(Account.class));
    }

    @Test
    @DisplayName("Update account to default successfully")
    public void updateAccountToDefaultSuccess() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(updateData.name()).thenReturn(null);
        when(updateData.enable()).thenReturn(null);
        when(updateData.isDefault()).thenReturn(true);
        when(accountGateway.update(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountGateway.findDefaultByUser(testUser)).thenReturn(Optional.of(defaultAccount));
        // Act
        Account result = updateAccountUseCase.execute(accountId, updateData);

        // Assert
        assertNotNull(result);
        assertTrue(result.isDefault());
        assertFalse(defaultAccount.isDefault());
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, times(1)).findById(accountId);
        verify(accountGateway, times(2)).update(any(Account.class));
    }

    @Test
    @DisplayName("Update non-existing account should throw AccessDeniedBusinessException")
    public void updateNonExistingAccountShouldThrowException() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            updateAccountUseCase.execute(accountId, updateData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, never()).verifyOwnershipOrAdmin(anyLong());
        verify(accountGateway, never()).update(any(Account.class));
    }

    @Test
    @DisplayName("Update account without permission should throw AccessDeniedBusinessException")
    public void updateAccountWithoutPermissionShouldThrowException() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            updateAccountUseCase.execute(accountId, updateData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, never()).update(any(Account.class));
    }

    @Test
    @DisplayName("Deactivate default account should throw CannotDeactivateDefaultAccountException")
    public void deactivateDefaultAccountShouldThrowException() {
        // Arrange
        testAccount.setDefault(true);
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(updateData.enable()).thenReturn(false);

        // Act & Assert
        assertThrows(CannotDeactivateDefaultAccountException.class, () -> {
            updateAccountUseCase.execute(accountId, updateData);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, never()).update(any(Account.class));
    }
}
