package com.finco.finco.usecase.account;

import java.time.LocalDateTime;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.exception.IncompatibleCurrencyException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.usecase.account.dto.IAccountTransferData;

public class TransferAccountUseCase {

    private final AccountGateway accountGateway;
    private final AuthGateway authGateway;
    private final TransactionGateway transactionGateway;

    public TransferAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway,
            TransactionGateway transactionGateway) {
        this.accountGateway = accountGateway;
        this.authGateway = authGateway;
        this.transactionGateway = transactionGateway;
    }

    public Account execute(Long id, IAccountTransferData data) {
        Account account = accountGateway.findById(id).orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(account.getUser().getId());

        Account transferAccount = accountGateway.findById(data.transferAccountId()).orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(transferAccount.getUser().getId());

        if (account.getCurrency() != transferAccount.getCurrency()) {
            throw new IncompatibleCurrencyException();
        }

        account.transfer(data.amount(), transferAccount);

        //account transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(data.amount());
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setDate(LocalDateTime.now());
        transaction.setUser(account.getUser());
        transaction.setTransferAccount(transferAccount);
        if (data.category() != null) {
            transaction.setCategory(data.category());
        }
        if (data.description() != null) {
            transaction.setDescription(data.description());
        }

        //transfer account transaction
        Transaction transferTransaction = new Transaction();
        transferTransaction.setAccount(transferAccount);
        transferTransaction.setAmount(data.amount());
        transferTransaction.setType(TransactionType.DEPOSIT);
        transferTransaction.setDate(LocalDateTime.now());
        transferTransaction.setUser(transferAccount.getUser());
        transferTransaction.setTransferAccount(account);
        if (data.category() != null) {
            transferTransaction.setCategory(data.category());
        }
        if (data.description() != null) {
            transferTransaction.setDescription(data.description());
        }

        transactionGateway.create(transaction);
        transactionGateway.create(transferTransaction);

        accountGateway.update(transferAccount);

        return accountGateway.update(account);
    }

}
