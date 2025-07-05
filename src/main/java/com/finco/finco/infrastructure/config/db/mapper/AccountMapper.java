package com.finco.finco.infrastructure.config.db.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.config.db.repository.AccountRepository;
import com.finco.finco.infrastructure.config.db.schema.AccountSchema;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

@Component
public class AccountMapper {

    private final UserMapper userMapper;
    private final AccountRepository accountRepository; 

    public AccountMapper(@Lazy UserMapper userMapper, AccountRepository accountRepository) {
        this.userMapper = userMapper;
        this.accountRepository = accountRepository;
    }

    public Account toAccount(AccountSchema accountSchema) {
        if (accountSchema == null) {
            return null;
        }

        Account account = new Account();
        account.setId(accountSchema.getId());
        account.setUser(userMapper.toLigthUser(accountSchema.getUser()));
        account.setName(accountSchema.getName());
        account.setType(accountSchema.getType());
        account.setBalance(accountSchema.getBalance());
        account.setCurrency(accountSchema.getCurrency());
        account.setCreationDate(accountSchema.getCreationDate());
        account.setDescription(accountSchema.getDescription());
        account.setDefault(accountSchema.isDefault());
        account.setEnable(accountSchema.isEnable());
        account.setDepositFee(accountSchema.getDepositFee());
        account.setWithdrawFee(accountSchema.getWithdrawFee());

        return account;

    }

    public Account toAccountWithoutUser(AccountSchema accountSchema) {
        if (accountSchema == null) {
            return null;
        }

        Account account = new Account();
        account.setId(accountSchema.getId());
        account.setUser(null);
        account.setName(accountSchema.getName());
        account.setType(accountSchema.getType());
        account.setBalance(accountSchema.getBalance());
        account.setCurrency(accountSchema.getCurrency());
        account.setCreationDate(accountSchema.getCreationDate());
        account.setDescription(accountSchema.getDescription());
        account.setDefault(accountSchema.isDefault());
        account.setEnable(accountSchema.isEnable());
        account.setDepositFee(accountSchema.getDepositFee());
        account.setWithdrawFee(accountSchema.getWithdrawFee());
        

        return account;

    }

    public AccountSchema toAccountSchema(Account account) {
        if (account == null) {
            return null;
        }

        if (account.getUser() == null || account.getUser().getId() == null) {
            throw new IllegalArgumentException("Account must be associated with an existing User (with an ID).");
        }

        AccountSchema accountSchema = new AccountSchema();
        accountSchema.setId(account.getId());
        
        // Create a minimal user schema with just the ID to prevent circular reference
        UserSchema userSchema = new UserSchema();
        userSchema.setId(account.getUser().getId());
        accountSchema.setUser(userSchema);
        
        accountSchema.setName(account.getName());
        accountSchema.setType(account.getType());
        accountSchema.setBalance(account.getBalance());
        accountSchema.setCurrency(account.getCurrency());
        accountSchema.setCreationDate(account.getCreationDate());
        accountSchema.setDescription(account.getDescription());
        accountSchema.setDefault(account.isDefault());
        accountSchema.setEnable(account.isEnable());
        accountSchema.setDepositFee(account.getDepositFee());
        accountSchema.setWithdrawFee(account.getWithdrawFee());

        Long version = accountRepository.getVersionByAccountId(account.getId());
        accountSchema.setVersion(version);

        return accountSchema;

    }

    public PagedResult<Account> toAccountPagedResult(Page<AccountSchema> accountSchemaPage, PageRequest page) {
        if (accountSchemaPage == null) {
            return PagedResult.empty(page);
        }

        List<Account> accountList = accountSchemaPage.getContent().stream()
                                            .map(this::toAccount)
                                            .collect(Collectors.toList());

        return new PagedResult<>(
            accountList,
            accountSchemaPage.getTotalElements(),
            accountSchemaPage.getTotalPages(),
            accountSchemaPage.getNumber(),
            accountSchemaPage.getSize(),
            accountSchemaPage.isFirst(),
            accountSchemaPage.isLast(),
            accountSchemaPage.hasNext(),
            accountSchemaPage.hasPrevious()
        );
    }

}
