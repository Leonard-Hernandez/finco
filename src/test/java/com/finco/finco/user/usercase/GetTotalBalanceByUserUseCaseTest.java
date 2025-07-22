package com.finco.finco.user.usercase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.GetTotalBalanceByUserUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get total balance by user test")
public class GetTotalBalanceByUserUseCaseTest {

    @Mock
    private AuthGateway authGateway;

    @Mock
    private AccountGateway accountGateway;

    @InjectMocks
    private GetTotalBalanceByUserUseCase getTotalBalanceByUserUseCase;

    private User user;
    private Account account;
    private Account account2;
    private Account account3;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("User One");

        account = new Account();
        account.setId(1L);
        account.setName("Account One");
        account.setType(AccountType.SAVING);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setUser(user);

        account2 = new Account();
        account2.setId(2L);
        account2.setName("Account Two");
        account2.setType(AccountType.SAVING);
        account2.setBalance(BigDecimal.valueOf(2000));
        account2.setUser(user);

        account3 = new Account();
        account3.setId(3L);
        account3.setName("Account Three");
        account3.setType(AccountType.CREDIT);
        account3.setBalance(BigDecimal.valueOf(-300));
        account3.setUser(user);
    }

    @Test
    @DisplayName("Get total balance by user successfully")
    public void getTotalBalanceByUserSuccess() {
        // Arrange
        doNothing().when(authGateway).verifyOwnershipOrAdmin(user.getId());
        when(accountGateway.findAllByUser(user.getId())).thenReturn(List.of(account, account2, account3));
        when(accountGateway.getTotalBalanceInGoalsByAccount(1L)).thenReturn(BigDecimal.valueOf(500));
        when(accountGateway.getTotalBalanceInGoalsByAccount(2L)).thenReturn(BigDecimal.valueOf(1000));
        when(accountGateway.getTotalBalanceInGoalsByAccount(3L)).thenReturn(BigDecimal.valueOf(0));

        // Act
        BigDecimal result = getTotalBalanceByUserUseCase.execute(user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(1200), result);
    }

    @Test
    @DisplayName("Get total balance negative balance")
    public void getTotalBalanceNegativeBalance() {
        // Arrange
        account3.setBalance(BigDecimal.valueOf(-10000));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(user.getId());
        when(accountGateway.findAllByUser(user.getId())).thenReturn(List.of(account, account2, account3));
        when(accountGateway.getTotalBalanceInGoalsByAccount(anyLong())).thenReturn(BigDecimal.valueOf(0));

        // Act
        BigDecimal result = getTotalBalanceByUserUseCase.execute(user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(-7000), result);
    }

    @Test
    @DisplayName("Get total without accounts")
    public void getTotalBalanceWithoutAccounts() {
        // Arrange
        doNothing().when(authGateway).verifyOwnershipOrAdmin(user.getId());
        when(accountGateway.findAllByUser(user.getId())).thenReturn(List.of());

        // Act
        BigDecimal result = getTotalBalanceByUserUseCase.execute(user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(0), result);
    }


}
