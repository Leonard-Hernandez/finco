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

    @Tool(description = "Retrieve a single account's full details (balance, currency, type, fees) by its numeric ID. Use only when user references a specific account by ID.")
    @LogExecution(logArguments = false, logReturnValue = false)
    public Account getAccount(@ToolParam(description = "Account numeric ID") Long id) {
        return getAccountUseCase.execute(id);
    }

    @Tool(description = "List the authenticated user's accounts. Use to find the user's accounts before any transaction or when user asks about their accounts. Returns a page of accounts with id, name, balance, currency, type.")
    @LogExecution(logArguments = false, logReturnValue = false)
    public List<AccountPublicData> getAllAccountsByUser(
            @ToolParam(description = "Page number (0-based). Default 0", required = false) Integer page,
            @ToolParam(description = "Page size. Default 20", required = false) Integer size,
            @ToolParam(description = "Sort field. Default id", required = false) String sortBy,
            @ToolParam(description = "Sort direction asc|desc. Default desc", required = false) String sortDirection,
            @ToolParam(description = "Filter by currency", required = false) CurrencyEnum currency,
            @ToolParam(description = "Filter by account type", required = false) AccountType type) {

        Long userId = authGateway.getAuthenticatedUserId();
        PageRequest domainPageRequest = toPageRequest(page == null ? 0 : page, size == null ? 20 : size, sortBy, sortDirection);
        AccountFilterData accountFilterData = new AccountFilterData(userId, currency, type, true);
        return getAllAccountsByUserUseCase.execute(domainPageRequest, accountFilterData).getContent().stream()
                .map(AccountPublicData::new).toList();
    }

    @Tool(description = "Deposit money into an account. ALWAYS confirm with the user before calling. Returns the updated account.")
    @LogExecution(logArguments = false, logReturnValue = false)
    public AccountPublicData deposit(
            @ToolParam(description = "Target account ID") Long accountId,
            @ToolParam(description = "Transaction data: amount, description, category, date") AccountTransactionData data) {
        return new AccountPublicData(depositAccountUseCase.execute(accountId, data));
    }

    @Tool(description = "Withdraw money from an account. ALWAYS confirm with the user before calling. Returns the updated account.")
    @LogExecution(logArguments = false, logReturnValue = false)
    public AccountPublicData withdraw(
            @ToolParam(description = "Source account ID") Long accountId,
            @ToolParam(description = "Transaction data: amount, description, category, date") AccountTransactionData data) {
        return new AccountPublicData(withDrawAccountUseCase.execute(accountId, data));
    }

    @Tool(description = "Transfer money between two accounts of the user. ALWAYS confirm with the user before calling. Returns the source account updated.")
    @LogExecution(logArguments = false, logReturnValue = false)
    public AccountPublicData transfer(
            @ToolParam(description = "Source account ID") Long accountId,
            @ToolParam(description = "Transfer data: target account ID, amount, description, category, date") AccountTransferData data) {
        return new AccountPublicData(transferAccountUseCase.execute(accountId, data));
    }

}
