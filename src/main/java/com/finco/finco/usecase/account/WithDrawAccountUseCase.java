package com.finco.finco.usecase.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.infrastructure.account.dto.AccountTransactionData;

public class WithDrawAccountUseCase {

    private final AccountGateway accountGateway;
    private final AuthGateway authGateway;
    private final TransactionGateway transactionGateway;

    public WithDrawAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway,
            TransactionGateway transactionGateway) {
        this.accountGateway = accountGateway;
        this.authGateway = authGateway;
        this.transactionGateway = transactionGateway;
    }

    @TransactionalDomainAnnotation
    public Account execute(Long id, AccountTransactionData data) {
        Account account = accountGateway.findById(id).orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(account.getUser().getId());

        account.withdraw(data.amount());

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(data.amount());
        transaction.setFee(data.amount().multiply(new BigDecimal(account.getWithdrawFee())));
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setDate(LocalDateTime.now());
        transaction.setUser(account.getUser());
        if (data.category() != null) {
            transaction.setCategory(data.category());
        }
        if (data.description() != null) {
            transaction.setDescription(data.description());
        }

        transactionGateway.create(transaction);

        return accountGateway.update(account);
    }

}
