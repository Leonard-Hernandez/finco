package com.finco.finco.entity.transaction.gateway;

import java.util.List;
import java.util.Optional;

import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;

public interface TransactionGateway {

    Transaction create(Transaction transaction);
    Transaction update(Transaction transaction);
    void delete(Transaction transaction);

    Optional<Transaction> findById(Long id);

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByUserIdAndType(Long userId, TransactionType type);

    List<Transaction> findByUserIdAndCategoryLike(Long userId, String search);

    List<Transaction> findByGoalId(Long goalId);

    List<Transaction> findByUserIdAndTransferAccountId(Long userId, Long TransferedId);

}
