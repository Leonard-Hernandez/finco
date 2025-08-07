package com.finco.finco.infrastructure.account.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.account.dto.AccountFilterData;
import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.usecase.account.GetAllAccountUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.finco.finco.infrastructure.config.error.ErrorResponse;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

@RestController
@Tag(name = "Account", description = "Account management endpoints")
public class GetAllAccountController {

    private final GetAllAccountUseCase getAllAccountUseCase;

    public GetAllAccountController(GetAllAccountUseCase getAllAccountUseCase) {
        this.getAllAccountUseCase = getAllAccountUseCase;
    }

    @GetMapping("/admin/accounts")
    @LogExecution()
    @Operation(summary = "Get all accounts", description = "Get all accounts need admin role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts found successfully", 
                content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be admin to get all accounts", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public Page<AccountPublicData> getAllAccounts(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
        @RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
        @RequestParam(name = "userId", required = false) Long userId,
        @RequestParam(name = "currency", required = false) CurrencyEnum currency,
        @RequestParam(name = "type", required = false) AccountType type,
        @RequestParam(name = "enable", required = false) Boolean enable) {

        PageRequest domainPageRequest = toPageRequest(page, size, sortBy, sortDirection);
        AccountFilterData accountFilterData = new AccountFilterData(userId, currency, type, enable);

        PagedResult<Account> accountsPagedResult = getAllAccountUseCase.execute(domainPageRequest, accountFilterData);

        return toPage(accountsPagedResult, toPageable(domainPageRequest)).map(AccountPublicData::new);
    }

}
