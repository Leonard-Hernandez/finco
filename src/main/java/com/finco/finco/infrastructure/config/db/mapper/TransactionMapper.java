package com.finco.finco.infrastructure.config.db.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.infrastructure.config.db.schema.TransactionSchema;

@Component
public class TransactionMapper {

    private final UserMapper userMapper;
    private final AccountMapper accountMapper;
    private final GoalMapper goalMapper;

    public TransactionMapper(@Lazy UserMapper userMapper, AccountMapper accountMapper, GoalMapper goalMapper) {
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.goalMapper = goalMapper;
    }

    public Transaction toTransaction(TransactionSchema transactionSchema) {
        if (transactionSchema == null) {
            return null;
        }

        Transaction transaction = new Transaction();

        transaction.setId(transactionSchema.getId());
        transaction.setUser(userMapper.toLigthUser(transactionSchema.getUser()));
        transaction.setAccount(accountMapper.toAccountWithoutUser(transactionSchema.getAccount()));
        transaction.setGoal(goalMapper.toGoalWithoutUser(transactionSchema.getGoal()));
        transaction.setAmount(transactionSchema.getAmount());
        transaction.setDate(transactionSchema.getDate());
        transaction.setDescription(transactionSchema.getDescription());
        transaction.setCategory(transactionSchema.getCategory());
        transaction.setTransferAccount(accountMapper.toAccountWithoutUser(transactionSchema.getTransferAccount()));

        return transaction;
    }

    public TransactionSchema toTransactionSchema(Transaction transaction) {

        if (transaction == null) {
            return null;
        }

        if (transaction.getUser() == null || transaction.getUser().getId() == null) {
            throw new IllegalArgumentException("Transaction must be associated with an existing User (with an ID).");
        }

        if (transaction.getAccount() == null || transaction.getAccount().getId() == null) {
            throw new IllegalArgumentException("Transaction must be associated with an existing Account (with an ID).");
        }

        TransactionSchema transactionSchema = new TransactionSchema();
        transactionSchema.setId(transaction.getId());
        transactionSchema.setUser(userMapper.toUserSchema(transaction.getUser()));
        transactionSchema.setAccount(accountMapper.toAccountSchema(transaction.getAccount()));
        transactionSchema.setType(transaction.getType());
        transactionSchema.setAmount(transaction.getAmount());
        transactionSchema.setDate(transaction.getDate());
        transactionSchema.setDescription(transaction.getDescription());
        transactionSchema.setCategory(transaction.getCategory());

        if (transaction.getGoal() != null && transaction.getGoal().getId() != null) {
            transactionSchema.setGoal(goalMapper.toGoalSchema(transaction.getGoal()));
        }

        if (transaction.getTransferAccount() != null && transaction.getTransferAccount().getId() != null) {
            transactionSchema.setTransferAccount(accountMapper.toAccountSchema(transaction.getTransferAccount()));
        }

        return transactionSchema;

    }

}
