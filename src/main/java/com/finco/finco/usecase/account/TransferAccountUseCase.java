package com.finco.finco.usecase.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finco.finco.entity.account.exception.ExchangeRateNotFound;
import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
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
        Account fromAccount = accountGateway.findById(id).orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(fromAccount.getUser().getId());

        Account transferAccount = accountGateway.findById(data.transferAccountId())
                .orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(transferAccount.getUser().getId());

        BigDecimal depositAmount = null;

        if (fromAccount.getCurrency() != transferAccount.getCurrency()) {

            if (data.exchangeRate() == null) {
                throw new ExchangeRateNotFound();
            }

            fromAccount.transfer(data.amount(), transferAccount, data.exchangeRate());
            depositAmount = data.amount()
                    .subtract(data.amount().multiply(new BigDecimal(fromAccount.getDepositFee())))
                    .multiply(data.exchangeRate());
        } else {
            fromAccount.transfer(data.amount(), transferAccount);
            depositAmount = data.amount()
                    .subtract(data.amount().multiply(new BigDecimal(fromAccount.getDepositFee())));
        }

        // account transaction
        createTransaction(fromAccount, transferAccount, data, TransactionType.WITHDRAW, data.amount());

        // transfer account transaction
        createTransaction(transferAccount, fromAccount, data, TransactionType.DEPOSIT, depositAmount);

        accountGateway.update(transferAccount);

        return accountGateway.update(fromAccount);
    }

    private void createTransaction(Account account, Account transferAccount, IAccountTransferData data,
            TransactionType type, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setDate(LocalDateTime.now());
        transaction.setUser(account.getUser());
        transaction.setTransferAccount(transferAccount);
        transaction.setType(type);
        if (type.equals(TransactionType.WITHDRAW)) {
            transaction.setAmount(amount.subtract(amount.multiply(new BigDecimal(account.getWithdrawFee()))));
            transaction.setFee(amount.multiply(new BigDecimal(account.getWithdrawFee())));
        } else if (type.equals(TransactionType.DEPOSIT)) {
            transaction.setAmount(amount);
            transaction.setFee(amount.multiply(new BigDecimal(account.getDepositFee())));
        }
        if (data.exchangeRate() != null) {
            transaction.setExchangeRate(data.exchangeRate());
        }
        if (data.category() != null) {
            transaction.setCategory(data.category());
        }
        if (data.description() != null) {
            transaction.setDescription(data.description());
        }

        transactionGateway.create(transaction);
    }

}
