package com.finco.finco.transaction.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.usecase.transaction.GetCategoriesByUserUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get Categories By User Test")
public class GetCategoriesByUserUseCaseTest {

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private AuthGateway authGateway;

    @InjectMocks
    private GetCategoriesByUserUseCase getCategoriesByUserUseCase;

    private final Long userId = 1L;
    private List<String> testCategories;

    @BeforeEach
    public void setUp() {
        testCategories = Arrays.asList("Food", "Transportation", "Entertainment", "Bills");
    }

    @Test
    @DisplayName("Get categories by user successfully")
    public void getCategoriesByUserSuccess() {
        // Arrange
        when(transactionGateway.findAllCategoriesByUserId(userId)).thenReturn(testCategories);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        List<String> result = getCategoriesByUserUseCase.execute(userId);

        // Assert
        assertNotNull(result);
        assertEquals(testCategories.size(), result.size());
        assertTrue(result.containsAll(testCategories));
        
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, times(1)).findAllCategoriesByUserId(userId);
    }

    @Test
    @DisplayName("Get empty categories list when user has no transactions with categories")
    public void getEmptyCategoriesListWhenUserHasNoTransactions() {
        // Arrange
        List<String> emptyList = List.of();
        when(transactionGateway.findAllCategoriesByUserId(userId)).thenReturn(emptyList);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        List<String> result = getCategoriesByUserUseCase.execute(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, times(1)).findAllCategoriesByUserId(userId);
    }

    @Test
    @DisplayName("Get categories without permission should throw AccessDeniedBusinessException")
    public void getCategoriesWithoutPermissionShouldThrowException() {
        // Arrange
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getCategoriesByUserUseCase.execute(userId);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, never()).findAllCategoriesByUserId(anyLong());
    }

    @Test
    @DisplayName("Get unique and sorted categories")
    public void getUniqueAndSortedCategories() {
        // Arrange
        List<String> expectedSortedUnique = List.of("Bills", "Food", "Transportation");
        
        when(transactionGateway.findAllCategoriesByUserId(userId)).thenReturn(expectedSortedUnique);
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        List<String> result = getCategoriesByUserUseCase.execute(userId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedSortedUnique.size(), result.size());
        assertTrue(result.containsAll(expectedSortedUnique));
        assertEquals(expectedSortedUnique, result); // Checks both content and order
        
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(transactionGateway, times(1)).findAllCategoriesByUserId(userId);
    }
}
