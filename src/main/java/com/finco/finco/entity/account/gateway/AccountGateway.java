package com.finco.finco.entity.account.gateway;

import java.util.Optional;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;

public interface AccountGateway {

    Account create(Account account);
    Account update(Account account);
    Account delete(Account account);

    Optional<Account> findById(Long id);

    Optional<Account> findDefaultByUserId(Long userId);

    PagedResult<Account> findAll(PageRequest page);

    PagedResult<Account> findAllByUser(PageRequest page, Long userId);

    Long getTotalByUser(Long userId);

}
