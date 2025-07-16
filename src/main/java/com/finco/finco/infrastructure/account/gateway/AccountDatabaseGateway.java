package com.finco.finco.infrastructure.account.gateway;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.config.db.mapper.AccountMapper;
import com.finco.finco.infrastructure.config.db.repository.AccountRepository;
import com.finco.finco.infrastructure.config.db.schema.AccountSchema;

@Component
public class AccountDatabaseGateway implements AccountGateway {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDatabaseGateway(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Account create(Account account) {
        return accountMapper.toAccount(accountRepository.save(accountMapper.toAccountSchema(account)));
    }

    @Override
    @Transactional
    public Account update(Account account) {
        return accountMapper.toAccount(accountRepository.save(accountMapper.toAccountSchema(account)));
    }

    @Override
    public Account delete(Account account) {
        account.setEnable(false);
        return accountMapper.toAccount(accountRepository.save(accountMapper.toAccountSchema(account)));
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id).map(accountMapper::toAccount);
    }

    @Override
    public PagedResult<Account> findAll(PageRequest page) {
        Sort sort = page.getSortBy()
                .map(sortBy -> {
                    Sort.Direction direction = page.getSortDirection()
                            .filter(d -> d.equalsIgnoreCase("desc"))
                            .map(d -> Sort.Direction.DESC)
                            .orElse(Sort.Direction.ASC);
                    return Sort.by(direction, sortBy);
                })
                .orElse(Sort.unsorted());

        Pageable springPageable = org.springframework.data.domain.PageRequest.of(
                page.getPageNumber(),
                page.getPageSize(),
                sort);

        Page<AccountSchema> accountSchemaPage = accountRepository.findAllByEnableTrue(springPageable);

        return accountMapper.toAccountPagedResult(accountSchemaPage, page);
    }

    @Override
    public PagedResult<Account> findAllByUser(PageRequest page, Long userId) {
        Sort sort = page.getSortBy()
                .map(sortBy -> {
                    Sort.Direction direction = page.getSortDirection()
                            .filter(d -> d.equalsIgnoreCase("desc"))
                            .map(d -> Sort.Direction.DESC)
                            .orElse(Sort.Direction.ASC);
                    return Sort.by(direction, sortBy);
                })
                .orElse(Sort.unsorted());

        Pageable springPageable = org.springframework.data.domain.PageRequest.of(
                page.getPageNumber(),
                page.getPageSize(),
                sort);

        Page<AccountSchema> accountSchemaPage = accountRepository.findAllByUserId(springPageable,userId);

        return accountMapper.toAccountPagedResult(accountSchemaPage, page);
    }

    @Override
    public Long getTotalByUser(Long userId) {
        return accountRepository.getTotalByUserId(userId);
    }

    @Override
    public Optional<Account> findDefaultByUserId(Long userId) {
        return accountRepository.findByIsDefaultTrueAndUserId(userId).map(accountMapper::toAccount);
    }

    @Override
    public BigDecimal getTotalBalanceInGoalsByAccount(Long accountId) {
        return accountRepository.getTotalBalanceInGoalsByAccount(accountId);
    }

}
