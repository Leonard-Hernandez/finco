package com.finco.finco.infrastructure.account.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.config.db.mapper.AccountMapper;
import com.finco.finco.infrastructure.config.db.repository.AccountRepository;

@Component
public class AccountDatabaseGateway implements AccountGateway{

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
    public void delete(Account account) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id).map(accountMapper::toAccount);
    }

    @Override
    public List<Account> findbyUser(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findbyUser'");
    }

    @Override
    public List<Account> findByUserAndType(User user, AccountType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUserAndType'");
    }

    @Override
    public Long getTotalByUser(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalByUser'");
    }

}
