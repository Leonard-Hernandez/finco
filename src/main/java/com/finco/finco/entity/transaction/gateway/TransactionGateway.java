package com.finco.finco.entity.transaction.gateway;

import java.util.List;
import java.util.Optional;

import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.transaction.model.Transaction;

public interface TransactionGateway {

    Transaction create(Transaction transaction);
    Transaction update(Transaction transaction);
    
    Optional<Transaction> findById(Long id);

    PagedResult<Transaction> findAllByUserId(Long userId, PageRequest page);

    PagedResult<Transaction> findAllByAccountId(Long accountId, PageRequest page);

    PagedResult<Transaction> findAllByGoalId(Long goalId, PageRequest page);

    PagedResult<Transaction> findAllByUserIdAndTransferAccountId(Long userId, Long TransferedId, PageRequest page);

    List<String> findAllCategoriesByUserId(Long userId);

}
