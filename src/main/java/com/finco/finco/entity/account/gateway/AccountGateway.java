package com.finco.finco.entity.account.gateway;

import java.util.List;
import java.util.Optional;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.user.model.User;

public interface AccountGateway {

    Account create(Account account);
    Account update(Account account);
    Account delete(Account account);

    Optional<Account> findById(Long id);

    List<Account> findByUser(User user);

    List<Account> findByUserAndType(User user , AccountType type);

    Long getTotalByUser(User user);

}
