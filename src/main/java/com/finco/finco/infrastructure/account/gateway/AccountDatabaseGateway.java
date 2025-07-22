package com.finco.finco.infrastructure.account.gateway;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.config.db.mapper.AccountMapper;
import com.finco.finco.infrastructure.config.db.repository.AccountRepository;
import com.finco.finco.infrastructure.config.db.schema.AccountSchema;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

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

        Pageable springPageable = toPageable(page);

        Page<AccountSchema> accountSchemaPage = accountRepository.findAllByEnableTrue(springPageable);

        return accountMapper.toAccountPagedResult(accountSchemaPage, page);
    }

    @Override
    public PagedResult<Account> findAllByUser(PageRequest page, Long userId) {

        Pageable springPageable = toPageable(page);

        Page<AccountSchema> accountSchemaPage = accountRepository.findAllByUserId(springPageable, userId);

        return accountMapper.toAccountPagedResult(accountSchemaPage, page);
    }

    @Override
    public List<Account> findAllByUser(Long userId) {
        return accountRepository.findAllByUserId(userId).stream().map(accountMapper::toAccount).collect(Collectors.toList());
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
        BigDecimal totalBalanceInGoalsByAccount = accountRepository.getTotalBalanceInGoalsByAccount(accountId);
        return totalBalanceInGoalsByAccount != null ? totalBalanceInGoalsByAccount : BigDecimal.ZERO;
    }

}
