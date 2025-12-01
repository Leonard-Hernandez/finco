package com.finco.finco.infrastructure.account.gateway;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPageRequest;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.account.dto.AccountFilterData;
import com.finco.finco.infrastructure.account.dto.AccountTransactionData;
import com.finco.finco.infrastructure.account.dto.AccountTransferData;
import com.finco.finco.usecase.account.DepositAccountUseCase;
import com.finco.finco.usecase.account.GetAccountUseCase;
import com.finco.finco.usecase.account.GetAllAccountsByUserUseCase;
import com.finco.finco.usecase.account.TransferAccountUseCase;
import com.finco.finco.usecase.account.WithDrawAccountUseCase;

@Service
public class AccountAiTools {

    private final GetAccountUseCase getAccountUseCase;
    private final GetAllAccountsByUserUseCase getAllAccountsByUserUseCase;
    private final DepositAccountUseCase depositAccountUseCase;
    private final TransferAccountUseCase transferAccountUseCase;
    private final WithDrawAccountUseCase withDrawAccountUseCase;

    public AccountAiTools(GetAccountUseCase getAccountUseCase, GetAllAccountsByUserUseCase getAllAccountsByUserUseCase,
            DepositAccountUseCase depositAccountUseCase, TransferAccountUseCase transferAccountUseCase,
            WithDrawAccountUseCase withDrawAccountUseCase) {
        this.getAccountUseCase = getAccountUseCase;
        this.getAllAccountsByUserUseCase = getAllAccountsByUserUseCase;
        this.depositAccountUseCase = depositAccountUseCase;
        this.transferAccountUseCase = transferAccountUseCase;
        this.withDrawAccountUseCase = withDrawAccountUseCase;
    }

    @Tool(description = "Get account by id")
    public Account getAccount(Long id) {
        return getAccountUseCase.execute(id);
    }

    @Tool(description = "Get all accounts by user id")
    public PagedResult<Account> getAllAccountsByUser(
            @ToolParam(description = "Page, default 0", required = true) Integer page,
            @ToolParam(description = "Size, default 20", required = true) Integer size,
            @ToolParam(description = "Sort by, default id", required = false) String sortBy, 
            @ToolParam(description = "Sort direction, default desc", required = false) String sortDirection,
            @ToolParam(description = "Currency", required = false) CurrencyEnum currency,
            @ToolParam(description = "Type", required = false)AccountType type,
            @ToolParam(description = "User id", required = true) Long userId) {

        PageRequest domainPageRequest = toPageRequest(page, size, sortBy, sortDirection);
        AccountFilterData accountFilterData = new AccountFilterData(userId, currency, type, true);
        return getAllAccountsByUserUseCase.execute(domainPageRequest, accountFilterData);
    }

    @Tool(description = "Deposit money to account")
    public void deposit(Long accountId, AccountTransactionData data) {
        depositAccountUseCase.execute(accountId, data);
    }

    @Tool(description = "Withdraw money from account")
    public void withdraw(Long accountId, AccountTransactionData data) {
        withDrawAccountUseCase.execute(accountId, data);
    }

    @Tool(description = "Transfer money between accounts")
    public void transfer(Long accountId, AccountTransferData data) {
        transferAccountUseCase.execute(accountId, data);
    }   


}
