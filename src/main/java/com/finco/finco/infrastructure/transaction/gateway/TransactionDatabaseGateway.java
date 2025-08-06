package com.finco.finco.infrastructure.transaction.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.pagination.filter.ITransactionFilterData;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPageable;
import com.finco.finco.infrastructure.config.db.mapper.TransactionMapper;
import com.finco.finco.infrastructure.config.db.repository.TransactionRepository;
import com.finco.finco.infrastructure.config.db.schema.TransactionSchema;
import com.finco.finco.infrastructure.config.db.specification.TransactionSchemeSpecification;

@Component
public class TransactionDatabaseGateway implements TransactionGateway {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionDatabaseGateway(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @LogExecution(logArguments = false)
    public Transaction create(Transaction transaction) {
        return transactionMapper.toTransaction(transactionRepository.save(transactionMapper.toTransactionSchema(transaction)));
    }

    @Override
    @LogExecution(logReturnValue = false, logArguments = false)
    public Transaction update(Transaction transaction) {
        return transactionMapper.toTransaction(transactionRepository.save(transactionMapper.toTransactionSchema(transaction)));
    }

    @Override
    @LogExecution(logReturnValue = false, logArguments = false)
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id).map(transactionMapper::toTransaction);
    }

    @Override
    public PagedResult<Transaction> findAllByFilterData(ITransactionFilterData filterData, PageRequest page) {
        Pageable springPageable = toPageable(page);
        Specification<TransactionSchema> specification = TransactionSchemeSpecification.getSpecification(filterData);
        return transactionMapper.toTransactionPagedResult(transactionRepository.findAll(specification, springPageable), page);
    }

    @Override
    @LogExecution(logReturnValue = false, logArguments = false)
    public List<String> findAllCategoriesByUserId(Long userId) {
        return transactionRepository.findAllCategoriesByUserId(userId);
    }

   

}
