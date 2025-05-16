package com.finco.finco.entity.account.gateway;

import java.util.List;
import java.util.Optional;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.AccountType;

public interface AccountGateway {

    Account create(Account account);
    Account update(Account account);
    void delete(Account account);

    Optional<Account> findById(Long id);

    List<Account> findbyUserId(Long userId);

    List<Account> findByUserIdAndType(Long userId , AccountType type);

    Long getTotalByUserId(Long userId);

}
