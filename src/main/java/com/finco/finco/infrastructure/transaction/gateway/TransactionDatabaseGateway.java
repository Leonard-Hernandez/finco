package com.finco.finco.infrastructure.transaction.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.infrastructure.config.db.repository.TransactionRepository;
import com.finco.finco.infrastructure.config.db.mapper.TransactionMapper;

@Component
public class TransactionDatabaseGateway implements TransactionGateway {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionDatabaseGateway(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public Transaction create(Transaction transaction) {
        return transactionMapper.toTransaction(transactionRepository.save(transactionMapper.toTransactionSchema(transaction)));
    }

    @Override
    public Transaction update(Transaction transaction) {
        return transactionMapper.toTransaction(transactionRepository.save(transactionMapper.toTransactionSchema(transaction)));
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id).map(transactionMapper::toTransaction);
    }

    @Override
    public List<Transaction> findAllByUserId(Long userId) {
        return transactionRepository.findAllByUserId(userId).stream().map(transactionMapper::toTransaction)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findAllByAccountId(Long accountId) {
        return transactionRepository.findAllByAccountId(accountId).stream().map(transactionMapper::toTransaction)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findAllByGoalId(Long goalId) {
        return transactionRepository.findAllByGoalId(goalId).stream().map(transactionMapper::toTransaction)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findAllByUserIdAndTransferAccountId(Long userId, Long TransferedId) {
        return transactionRepository.findAllByUserIdAndTransferAccountId(userId, TransferedId).stream()
                .map(transactionMapper::toTransaction).collect(Collectors.toList());
    }

}
