package com.finco.finco.infrastructure.config.db.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.config.db.repository.AccountRepository;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.AccountSchema;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

@Component
public class AccountMapper {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository; 

    public AccountMapper(@Lazy UserMapper userMapper, UserRepository userRepository, AccountRepository accountRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
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

        return account;

    }

    public AccountSchema toAccountSchema(Account account) {

        if (account == null) {
            return null;
        }

        UserSchema userSchema = null;
        User userDomain = account.getUser();

        if (userDomain == null || userDomain.getId() == null) {
            throw new IllegalArgumentException("Account must be associated with an existing User (with an ID).");
        }

        userSchema = userRepository.findById(userDomain.getId())
                .orElseThrow(() -> new RuntimeException("user not found"));

        AccountSchema accountSchema = new AccountSchema();
        accountSchema.setId(account.getId());
        accountSchema.setUser(userSchema);
        accountSchema.setName(account.getName());
        accountSchema.setType(account.getType());
        accountSchema.setBalance(account.getBalance());
        accountSchema.setCurrency(account.getCurrency());
        accountSchema.setCreationDate(account.getCreationDate());
        accountSchema.setDescription(account.getDescription());
        accountSchema.setDefault(account.isDefault());

        Long version = accountRepository.getVersionByAccountId(account.getId());

        accountSchema.setVersion(version);

        return accountSchema;

    }

}
