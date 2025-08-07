package com.finco.finco.account.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import com.finco.finco.entity.exception.EbusinessException;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.pagination.filter.IAccountFilterData;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.account.dto.AccountFilterData;
import com.finco.finco.usecase.account.GetAllAccountsByUserUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get all accounts by user test")
public class GetAllAccountsByUserUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private GetAllAccountsByUserUseCase getAllAccountsByUserUseCase;

    private final Long userId = 1L;
    private User testUser;

    private final Long accountId = 1L;
    private Account testAccount;

    private final Long account2Id = 2L;
    private Account testAccount2;

    private final Long account3Id = 3L;
    private Account testAccount3;

    private PageRequest pageRequest;
    private PagedResult<Account> pagedResult;    

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");

        pageRequest = new PageRequest(0, 10, "name", "asc");

        testAccount = new Account();
        testAccount.setId(accountId);
        testAccount.setName("Test Account");
        testAccount.setBalance(BigDecimal.valueOf(1000));
        testAccount.setUser(testUser);
        testAccount.setEnable(true);
        testAccount.setDefault(true);

        testAccount2 = new Account();
        testAccount2.setId(account2Id);
        testAccount2.setName("Test Account 2");
        testAccount2.setBalance(BigDecimal.valueOf(2000));
        testAccount2.setUser(testUser);
        testAccount2.setEnable(true);
        testAccount2.setDefault(false);

        testAccount3 = new Account();
        testAccount3.setId(account3Id);
        testAccount3.setName("Test Account 3");
        testAccount3.setBalance(BigDecimal.valueOf(3000));
        testAccount3.setUser(testUser);
        testAccount3.setEnable(true);
        testAccount3.setDefault(false);

        pagedResult = new PagedResult<>(List.of(testAccount, testAccount2, testAccount3), 3, 1, 0, 10, true, false, false, false);
    }

    @Test
    @DisplayName("Get all accounts by user successfully")
    public void getAllAccountsByUserSuccess() {
        // Arrange
        IAccountFilterData filterData = new AccountFilterData(userId, null, null, null);
        when(accountGateway.findByFilterData(any(PageRequest.class), any(IAccountFilterData.class))).thenReturn(pagedResult);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        PagedResult<Account> result = getAllAccountsByUserUseCase.execute(pageRequest, filterData);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, times(1)).findByFilterData(any(PageRequest.class), any(IAccountFilterData.class));
    }

    @Test
    @DisplayName("Get all accounts by user without ownership throw AccessDeniedBusinessException")
    public void getAllAccountsByUserWithoutOwnershipShouldThrowException() {
        // Arrange
        IAccountFilterData filterData = new AccountFilterData(userId, null, null, null);
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        EbusinessException exception = assertThrows(EbusinessException.class, () -> {
            getAllAccountsByUserUseCase.execute(pageRequest, filterData);
        });

        assertEquals("Access Denied for this resource", exception.getMessage());
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(accountGateway, never()).findByFilterData(any(PageRequest.class), any(IAccountFilterData.class));
    }

}
