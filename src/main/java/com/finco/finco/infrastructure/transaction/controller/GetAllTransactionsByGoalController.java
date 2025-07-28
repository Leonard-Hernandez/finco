package com.finco.finco.infrastructure.transaction.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.infrastructure.transaction.dto.TransactionPublicData;
import com.finco.finco.usecase.transaction.GetAllTransactionsByGoalsUseCase;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

import java.util.stream.Collectors;

@RestController
public class GetAllTransactionsByGoalController {

    private final GetAllTransactionsByGoalsUseCase getAllTransactionsByGoalsUseCase;

    public GetAllTransactionsByGoalController(GetAllTransactionsByGoalsUseCase getAllTransactionsByGoalsUseCase) {
        this.getAllTransactionsByGoalsUseCase = getAllTransactionsByGoalsUseCase;
    }

    @GetMapping("/goals/{goalId}/transactions")
    @LogExecution()
    public Page<TransactionPublicData> getAllTransactionsByGoal(
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Direction.DESC) Pageable pageable,
            @PathVariable Long goalId) {
        PageRequest domainPageRequest = toPageRequest(pageable);
        PagedResult<Transaction> transactionsPagedResult = getAllTransactionsByGoalsUseCase.execute(domainPageRequest,
                goalId);

        return new PageImpl<>(transactionsPagedResult.getContent().stream()
                .map(TransactionPublicData::new).collect(Collectors.toList()), pageable,
                transactionsPagedResult.getTotalElements());
    }

}
