package com.finco.finco.entity.transaction.gateway;

import java.util.List;
import java.util.Optional;

import com.finco.finco.entity.transaction.model.Transaction;

public interface TransactionGateway {

    Transaction create(Transaction transaction);
    Transaction update(Transaction transaction);
    
    Optional<Transaction> findById(Long id);

    List<Transaction> findAllByUserId(Long userId);

    List<Transaction> findAllByAccountId(Long accountId);

    List<Transaction> findAllByGoalId(Long goalId);

    List<Transaction> findAllByUserIdAndTransferAccountId(Long userId, Long TransferedId);

}
