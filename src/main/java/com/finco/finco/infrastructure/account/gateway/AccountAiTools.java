package com.finco.finco.infrastructure.account.gateway;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.infrastructure.account.dto.AccountFilterData;
import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.account.dto.AccountTransactionData;
import com.finco.finco.infrastructure.account.dto.AccountTransferData;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPageRequest;
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
    private final AuthGateway authGateway;

    public AccountAiTools(GetAccountUseCase getAccountUseCase, GetAllAccountsByUserUseCase getAllAccountsByUserUseCase,
            DepositAccountUseCase depositAccountUseCase, TransferAccountUseCase transferAccountUseCase,
            WithDrawAccountUseCase withDrawAccountUseCase, AuthGateway authGateway) {
        this.getAccountUseCase = getAccountUseCase;
        this.getAllAccountsByUserUseCase = getAllAccountsByUserUseCase;
        this.depositAccountUseCase = depositAccountUseCase;
        this.transferAccountUseCase = transferAccountUseCase;
        this.withDrawAccountUseCase = withDrawAccountUseCase;
        this.authGateway = authGateway;
    }

    @Tool(description = "Get account by id")
    @LogExecution(logArguments = false, logReturnValue = false)
    public Account getAccount(Long id) {
        if (!authGateway.isAuthenticatedUserInRole("PREMIUM")) {
            throw new AccessDeniedBusinessException();
        }
        return getAccountUseCase.execute(id);
    }

    @Tool(description = "Get all accounts by user id")
    @LogExecution(logArguments = false, logReturnValue = false)
    public List<AccountPublicData> getAllAccountsByUser(
            @ToolParam(description = "Page, default 0", required = true) Integer page,
            @ToolParam(description = "Size, default 20", required = true) Integer size,
            @ToolParam(description = "Sort by, default id", required = false) String sortBy,
            @ToolParam(description = "Sort direction, default desc", required = false) String sortDirection,
            @ToolParam(description = "Currency", required = false) CurrencyEnum currency,
            @ToolParam(description = "Type", required = false) AccountType type,
            @ToolParam(description = "User id", required = true) Long userId) {

        if (!authGateway.isAuthenticatedUserInRole("PREMIUM")) {
            throw new AccessDeniedBusinessException();
        }

        PageRequest domainPageRequest = toPageRequest(page, size, sortBy, sortDirection);
        AccountFilterData accountFilterData = new AccountFilterData(userId, currency, type, true);
        return getAllAccountsByUserUseCase.execute(domainPageRequest, accountFilterData).getContent().stream()
                .map(AccountPublicData::new).toList();
    }

    @Tool(description = "Deposit money to account")
    @LogExecution(logArguments = false, logReturnValue = false)
    public AccountPublicData deposit(Long accountId, AccountTransactionData data) {
        if (!authGateway.isAuthenticatedUserInRole("PREMIUM")) {
            throw new AccessDeniedBusinessException();
        }
        return new AccountPublicData(depositAccountUseCase.execute(accountId, data));
    }

    @Tool(description = "Withdraw money from account")
    @LogExecution(logArguments = false, logReturnValue = false)
    public AccountPublicData withdraw(Long accountId, AccountTransactionData data) {
        if (!authGateway.isAuthenticatedUserInRole("PREMIUM")) {
            throw new AccessDeniedBusinessException();
        }
        return new AccountPublicData(withDrawAccountUseCase.execute(accountId, data));
    }

    @Tool(description = "Transfer money between accounts")
    @LogExecution(logArguments = false, logReturnValue = false)
    public AccountPublicData transfer(Long accountId, AccountTransferData data) {
        if (!authGateway.isAuthenticatedUserInRole("PREMIUM")) {
            throw new AccessDeniedBusinessException();
        }
        return new AccountPublicData(transferAccountUseCase.execute(accountId, data));
    }

}
