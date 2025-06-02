package com.finco.finco.infrastructure.config.db.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.infrastructure.config.db.repository.AccountRepository;
import com.finco.finco.infrastructure.config.db.repository.GoalRepository;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.AccountSchema;
import com.finco.finco.infrastructure.config.db.schema.GoalSchema;
import com.finco.finco.infrastructure.config.db.schema.TransactionSchema;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;



@Component
public class TransactionMapper {

    private final UserMapper userMapper;
    private final AccountMapper accountMapper;
    private final GoalMapper goalMapper;

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final GoalRepository goalRepository;

    public TransactionMapper(@Lazy UserMapper userMapper, AccountMapper accountMapper, GoalMapper goalMapper,
            UserRepository userRepository, AccountRepository accountRepository, GoalRepository goalRepository) {
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.goalMapper = goalMapper;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.goalRepository = goalRepository;
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

        UserSchema userSchema = null;
        AccountSchema accountSchema = null;
        GoalSchema goalSchema = null;
        AccountSchema transferAccountSchema = null;

        if (transaction.getUser() == null || transaction.getUser().getId() == null) {
            throw new IllegalArgumentException("Transaction must be associated with an existing User (with an ID).");
        }
        userSchema = userRepository.findById(transaction.getUser().getId())
                         .orElseThrow(() -> new RuntimeException("UserSchema not found for ID: " + transaction.getUser().getId() + " associated with transaction."));

        if (transaction.getAccount() == null || transaction.getAccount().getId() == null) {
            throw new IllegalArgumentException("Transaction must be associated with an existing Account (with an ID).");
        }
        accountSchema = accountRepository.findById(transaction.getAccount().getId())
                            .orElseThrow(() -> new RuntimeException("AccountSchema not found for ID: " + transaction.getAccount().getId() + " associated with transaction."));

        if (transaction.getGoal() != null && transaction.getGoal().getId() != null) {
            goalSchema = goalRepository.findById(transaction.getGoal().getId())
                            .orElseThrow(() -> new RuntimeException("GoalSchema not found for ID: " + transaction.getGoal().getId() + " associated with transaction."));
        }

        if (transaction.getTransferAccount() != null && transaction.getTransferAccount().getId() != null) {
            transferAccountSchema = accountRepository.findById(transaction.getTransferAccount().getId())
                            .orElseThrow(() -> new RuntimeException("TransferAccountSchema not found for ID: " + transaction.getTransferAccount().getId() + " associated with transaction."));
        }

        TransactionSchema transactionSchema = new TransactionSchema();
        transactionSchema.setId(transaction.getId());
        transactionSchema.setUser(userSchema);
        transactionSchema.setAccount(accountSchema);
        transactionSchema.setType(transactionSchema.getType());
        transactionSchema.setAmount(transaction.getAmount());
        transactionSchema.setDate(transaction.getDate());
        transactionSchema.setDescription(transaction.getDescription());
        transactionSchema.setCategory(transaction.getCategory());
        transactionSchema.setGoal(goalSchema);
        transactionSchema.setTransferAccount(transferAccountSchema);

        return transactionSchema;

    }

}
