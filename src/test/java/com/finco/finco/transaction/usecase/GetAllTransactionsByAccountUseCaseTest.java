package com.finco.finco.transaction.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.transaction.GetAllTransactionsByAccountUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get All Transactions By Account Test")
public class GetAllTransactionsByAccountUseCaseTest {

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private GetAllTransactionsByAccountUseCase getAllTransactionsByAccountUseCase;

    private User testUser;
    private Account testAccount;
    private Transaction testTransaction;
    private final Long userId = 1L;
    private final Long accountId = 1L;
    private PageRequest pageRequest;
    private PagedResult<Transaction> pagedResult;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setEnable(true);

        testAccount = new Account();
        testAccount.setId(accountId);
        testAccount.setUser(testUser);
        testAccount.setBalance(BigDecimal.valueOf(1000));

        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setUser(testUser);
        testTransaction.setAccount(testAccount);
        testTransaction.setAmount(BigDecimal.valueOf(100));
        testTransaction.setType(TransactionType.DEPOSIT);
        testTransaction.setDate(LocalDateTime.now());

        pageRequest = new PageRequest(0, 10, "date", "DESC");
        pagedResult = new PagedResult<>(List.of(testTransaction), 1, 1, 0, 10, true, false, false, false);
    }

    @Test
    @DisplayName("Get all transactions by account successfully")
    public void getAllTransactionsByAccountSuccess() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(transactionGateway.findAllByAccountId(accountId, pageRequest)).thenReturn(pagedResult);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        PagedResult<Transaction> result = getAllTransactionsByAccountUseCase.execute(pageRequest, accountId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(testTransaction.getId(), result.getContent().get(0).getId());
        
        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, times(1)).findAllByAccountId(accountId, pageRequest);
    }

    @Test
    @DisplayName("Get empty transactions list when account has no transactions")
    public void getEmptyTransactionsListWhenAccountHasNoTransactions() {
        // Arrange
        PagedResult<Transaction> emptyResult = new PagedResult<>(List.of(), 0, 0, 0, 10, true, false, false, false);
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(transactionGateway.findAllByAccountId(accountId, pageRequest)).thenReturn(emptyResult);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        PagedResult<Transaction> result = getAllTransactionsByAccountUseCase.execute(pageRequest, accountId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        
        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, times(1)).findAllByAccountId(accountId, pageRequest);
    }

    @Test
    @DisplayName("Get transactions for non-existent account should throw AccessDeniedBusinessException")
    public void getTransactionsForNonExistentAccountShouldThrowException() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getAllTransactionsByAccountUseCase.execute(pageRequest, accountId);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, never()).verifyOwnershipOrAdmin(anyLong());
        verify(transactionGateway, never()).findAllByAccountId(anyLong(), any());
    }

    @Test
    @DisplayName("Get transactions without permission should throw AccessDeniedBusinessException")
    public void getTransactionsWithoutPermissionShouldThrowException() {
        // Arrange
        when(accountGateway.findById(accountId)).thenReturn(Optional.of(testAccount));
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getAllTransactionsByAccountUseCase.execute(pageRequest, accountId);
        });

        verify(accountGateway, times(1)).findById(accountId);
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, never()).findAllByAccountId(anyLong(), any());
    }
}
