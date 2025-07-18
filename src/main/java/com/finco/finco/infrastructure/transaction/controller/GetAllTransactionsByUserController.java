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
import com.finco.finco.usecase.transaction.GetAllTransactionsByUserUseCase;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

@RestController
public class GetAllTransactionsByUserController {

    private final GetAllTransactionsByUserUseCase getAllTransactionsByUserUseCase;

    public GetAllTransactionsByUserController(GetAllTransactionsByUserUseCase getAllTransactionsByUserUseCase) {
        this.getAllTransactionsByUserUseCase = getAllTransactionsByUserUseCase;
    }

    @GetMapping("/users/{userId}/transactions")
    public Page<TransactionPublicData> getAllTransactionsByUser(
            @PageableDefault(page = 0, size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long userId) {
        PageRequest domainPageRequest = toPageRequest(pageable);
        PagedResult<Transaction> transactionsPagedResult = getAllTransactionsByUserUseCase.execute(domainPageRequest,
                userId);

        Page<TransactionPublicData> responsePage = toPage(transactionsPagedResult, pageable)
                .map(TransactionPublicData::new);

        return responsePage;
    }

}
