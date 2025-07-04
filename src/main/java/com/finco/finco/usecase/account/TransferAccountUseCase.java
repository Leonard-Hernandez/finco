package com.finco.finco.usecase.account;

import java.math.BigDecimal;
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

        Account transferAccount = accountGateway.findById(data.transferAccountId())
                .orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(transferAccount.getUser().getId());

        if (account.getCurrency() != transferAccount.getCurrency()) {
            throw new IncompatibleCurrencyException();
        }

        account.transfer(data.amount(), transferAccount);

        // account transaction
        createTransaction(account, transferAccount, data, TransactionType.WITHDRAW, data.amount());

        BigDecimal depositAmount = data.amount()
                .subtract(data.amount().multiply(new BigDecimal(account.getDepositFee())));

        // transfer account transaction
        createTransaction(transferAccount, account, data, TransactionType.DEPOSIT, depositAmount);

        accountGateway.update(transferAccount);

        return accountGateway.update(account);
    }

    private void createTransaction(Account account, Account transferAccount, IAccountTransferData data,
            TransactionType type, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        if (type.equals(TransactionType.WITHDRAW)) {
            transaction.setAmount(amount.subtract(amount.multiply(new BigDecimal(account.getWithdrawFee()))));
            transaction.setFee(amount.multiply(new BigDecimal(account.getWithdrawFee())));
        } else if (type.equals(TransactionType.DEPOSIT)) {
            transaction.setAmount(amount.subtract(amount.multiply(new BigDecimal(account.getDepositFee()))));
            transaction.setFee(amount.multiply(new BigDecimal(account.getDepositFee())));
        }
        transaction.setDate(LocalDateTime.now());
        transaction.setUser(account.getUser());
        transaction.setTransferAccount(transferAccount);
        if (data.category() != null) {
            transaction.setCategory(data.category());
        }
        if (data.description() != null) {
            transaction.setDescription(data.description());
        }

        if (type.equals(TransactionType.WITHDRAW)) {
            transaction.setType(TransactionType.WITHDRAW);
            if (data.withdrawFee() != null) {
                transaction.setFee(data.amount().multiply(new BigDecimal(account.getWithdrawFee())));
            }
        } else if (type.equals(TransactionType.DEPOSIT)) {
            transaction.setType(TransactionType.DEPOSIT);
            if (data.depositFee() != null) {
                transaction.setFee(data.amount().multiply(new BigDecimal(account.getDepositFee())));
            }
        }

        transactionGateway.create(transaction);
    }

}
