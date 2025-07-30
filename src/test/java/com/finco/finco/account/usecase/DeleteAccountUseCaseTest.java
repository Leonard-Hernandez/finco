package com.finco.finco.account.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.finco.finco.entity.account.exception.CannotDeactivateDefaultAccountException;
import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.goal.exception.BalanceInGoalException;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.account.DeleteAccountUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Delete account test")
public class DeleteAccountUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private DeleteAccountUseCase deleteAccountUseCase;

    private Account account;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEnable(true);

        account = new Account();
        account.setId(1L);
        account.setEnable(true);
        account.setUser(user);

    }

    @Test
    @DisplayName("Delete account successfully")
    public void deleteAccount() {

        // Arrange
        when(accountGateway.findById(1L)).thenReturn(Optional.of(account));
        when(accountGateway.getTotalBalanceInGoalsByAccount(account.getId())).thenReturn(BigDecimal.ZERO);
        when(accountGateway.delete(account)).thenAnswer(invocation -> {
            Account acc = invocation.getArgument(0);
            acc.setEnable(false);
            return acc;
        });
        doNothing().when(authGateway).verifyOwnershipOrAdmin(account.getUser().getId());

        // Act
        Account result = deleteAccountUseCase.execute(1L);

        // Assert
        assertNotNull(result);
        assertEquals(false, result.isEnable());
        verify(accountGateway, times(1)).findById(1L);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(account.getUser().getId());
        verify(accountGateway, times(1)).delete(account);

    }

    @Test
    @DisplayName("Delete non-existing account should throw AccessDeniedBusinessException")
    public void deleteAccountWithNonExistingAccount() {

        // Arrange
        when(accountGateway.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            deleteAccountUseCase.execute(1L);
        });

        verify(accountGateway, times(1)).findById(1L);
        verify(accountGateway, never()).delete(account);
    }

    @Test
    @DisplayName("Delete account without permission should throw AccessDeniedBusinessException")
    public void deleteAccountWithoutPermission() {
        
        // Arrange
        when(accountGateway.findById(1L)).thenReturn(Optional.of(account));
        doThrow(AccessDeniedBusinessException.class).when(authGateway)
                .verifyOwnershipOrAdmin(account.getUser().getId());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            deleteAccountUseCase.execute(1L);
        });

        verify(accountGateway, times(1)).findById(1L);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(account.getUser().getId());
        verify(accountGateway, never()).delete(account);
    }

    @Test
    @DisplayName("Deactivate default account should throw CannotDeactivateDefaultAccountException")
    public void deactivateDefaultAccount() {

        // Arrange
        when(accountGateway.findById(1L)).thenReturn(Optional.of(account));
        when(accountGateway.getTotalBalanceInGoalsByAccount(account.getId())).thenReturn(BigDecimal.ZERO);
        doThrow(CannotDeactivateDefaultAccountException.class).when(accountGateway).delete(account);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(account.getUser().getId());

        // Act & Assert
        assertThrows(CannotDeactivateDefaultAccountException.class, () -> {
            deleteAccountUseCase.execute(1L);
        });

        verify(accountGateway, times(1)).findById(1L);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(account.getUser().getId());
    }

    @Test
    @DisplayName("Delete account with balance in goals should throw BalanceInGoalException")
    public void deleteAccountWithBalanceInGoals() {

        // Arrange
        when(accountGateway.findById(1L)).thenReturn(Optional.of(account));
        when(accountGateway.getTotalBalanceInGoalsByAccount(account.getId())).thenReturn(new BigDecimal(100));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(account.getUser().getId());

        // Act & Assert
        assertThrows(BalanceInGoalException.class, () -> {
            deleteAccountUseCase.execute(1L);
        });

        verify(accountGateway, times(1)).findById(1L);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(account.getUser().getId());
    }

}
