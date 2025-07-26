package com.finco.finco.infrastructure.transaction.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.infrastructure.transaction.dto.TransactionPublicData;
import com.finco.finco.usecase.transaction.GetAllTransactionsByAccountUseCase;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

@RestController
public class GetAllTransactionsByAccountController {

    private final GetAllTransactionsByAccountUseCase getAllTransactionsByAccountUseCase;

    public GetAllTransactionsByAccountController(
            GetAllTransactionsByAccountUseCase getAllTransactionsByAccountUseCase) {
        this.getAllTransactionsByAccountUseCase = getAllTransactionsByAccountUseCase;
    }

    @GetMapping("/accounts/{accountId}/transactions")
    @LogExecution()
    public Page<TransactionPublicData> getAllTransactionsByAccount(
            @PageableDefault(page = 0, size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long accountId) {
        PageRequest domainPageRequest = toPageRequest(pageable);
        PagedResult<Transaction> transactionsPagedResult = getAllTransactionsByAccountUseCase.execute(domainPageRequest,
                accountId);

        Page<TransactionPublicData> responsePage = toPage(transactionsPagedResult, pageable)
                .map(TransactionPublicData::new);

        return responsePage;
    }
}
