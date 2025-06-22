package com.finco.finco.usecase.account;

import com.finco.finco.entity.account.exception.AccountNotFoundException;
import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.usecase.account.dto.IAccountUpdateData;

public class UpdateAccountUseCase {

    private final AccountGateway accountGateway;
    private final AuthGateway authGateway;
   
    public UpdateAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        this.accountGateway = accountGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation
    public Account execute(Long id, IAccountUpdateData data) {

        Account account = accountGateway.findById(id).orElseThrow(AccountNotFoundException::new);

        authGateway.verifyOwnershipOrAdmin(account.getUser().getId());

        if (data.name() != null || !data.name().trim().isEmpty()) {
            account.setName(data.name());
        }
        if (data.type() != null) {
            account.setType(data.type());
        }
        if (data.currency() != null) {
            account.setCurrency(data.currency());
        }
        if (data.description() != null || !data.description().trim().isEmpty()) {
            account.setDescription(data.description());
        }
        if (data.isDefault() != null) {
            account.setDefault(data.isDefault());
        }
        if (data.enable() != null) {
            account.setEnable(data.enable());
        }

        return accountGateway.update(account);

    }

}
