package com.finco.finco.transaction.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.exception.EbusinessException;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.pagination.filter.ITransactionFilterData;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.transaction.dto.TransactionFilterData;
import com.finco.finco.usecase.transaction.GetAllTransactionsByUserUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get All Transactions By User Test")
public class GetAllTransactionsByUserUseCaseTest {

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private GetAllTransactionsByUserUseCase getAllTransactionsByUserUseCase;

    private User testUser;
    private Transaction testTransaction;
    private final Long userId = 1L;
    private PageRequest pageRequest;
    private PagedResult<Transaction> pagedResult;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setEnable(true);

        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setUser(testUser);
        testTransaction.setAmount(BigDecimal.valueOf(100));
        testTransaction.setType(TransactionType.DEPOSIT);
        testTransaction.setDate(LocalDateTime.now());

        pageRequest = new PageRequest(0, 10, "date", "DESC");
        pagedResult = new PagedResult<>(List.of(testTransaction), 1, 1, 0, 10, true, false, false, false);
    }

    @Test
    @DisplayName("Get all transactions by user successfully")
    public void getAllTransactionsByUserSuccess() {
        // Arrange
        ITransactionFilterData filterData = new TransactionFilterData(userId, null, null, null, null, null);
        when(transactionGateway.findAllByFilterData(any(ITransactionFilterData.class), any(PageRequest.class)))
                .thenReturn(pagedResult);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        PagedResult<Transaction> result = getAllTransactionsByUserUseCase.execute(pageRequest, filterData);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(testTransaction.getId(), result.getContent().get(0).getId());

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, times(1)).findAllByFilterData(filterData, pageRequest);
    }

    @Test
    @DisplayName("Get empty transactions list when user has no transactions")
    public void getEmptyTransactionsListWhenUserHasNoTransactions() {
        // Arrange
        ITransactionFilterData filterData = new TransactionFilterData(userId, null, null, null, null, null);
        PagedResult<Transaction> emptyResult = new PagedResult<>(List.of(), 0, 0, 0, 10, true, false, false, false);
        when(transactionGateway.findAllByFilterData(any(ITransactionFilterData.class), any(PageRequest.class)))
                .thenReturn(emptyResult);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        PagedResult<Transaction> result = getAllTransactionsByUserUseCase.execute(pageRequest, filterData);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, times(1)).findAllByFilterData(any(ITransactionFilterData.class),
                any(PageRequest.class));
    }

    @Test
    @DisplayName("Get transactions without permission should throw AccessDeniedBusinessException")
    public void getTransactionsWithoutPermissionShouldThrowException() {
        // Arrange
        ITransactionFilterData filterData = new TransactionFilterData(userId, null, null, null, null, null);
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(anyLong());

        // Act & Assert
        EbusinessException exception = assertThrows(EbusinessException.class, () -> {
            getAllTransactionsByUserUseCase.execute(pageRequest, filterData);
        });

        assertNotNull(exception);
        assertEquals("Access Denied for this resource", exception.getMessage());
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, never()).findAllByFilterData(any(ITransactionFilterData.class),
                any(PageRequest.class));
    }

    @Test
    @DisplayName("Get transactions with pagination")
    public void getTransactionsWithPagination() {
        // Arrange
        PageRequest customPageRequest = new PageRequest(1, 5, "date", "DESC");
        ITransactionFilterData filterData = new TransactionFilterData(userId, null, null, null, null, null);
        when(transactionGateway.findAllByFilterData(any(ITransactionFilterData.class), eq(customPageRequest)))
                .thenReturn(pagedResult);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        PagedResult<Transaction> result = getAllTransactionsByUserUseCase.execute(customPageRequest, filterData);

        // Assert
        assertNotNull(result);
        verify(transactionGateway, times(1)).findAllByFilterData(any(ITransactionFilterData.class),
                eq(customPageRequest));
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
    }
}
