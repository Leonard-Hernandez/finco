package com.finco.finco.usecase.account;

import com.finco.finco.entity.account.exception.CannotDeactivateDefaultAccountException;
import com.finco.finco.entity.account.exception.DefaultAccountNotFoundException;
import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
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
    @LogExecution(logReturnValue = false, logArguments = false)
    public Account execute(Long id, IAccountUpdateData data) {

        Account account = accountGateway.findById(id).orElseThrow(AccessDeniedBusinessException::new);

        authGateway.verifyOwnershipOrAdmin(account.getUser().getId());

        if (data.name() != null && !data.name().isBlank()) {
            account.setName(data.name());
        }
        if (data.type() != null) {
            account.setType(data.type());
        }
        if (data.currency() != null) {
            account.setCurrency(data.currency());
        }
        if (data.description() != null && !data.description().isBlank()) {
            account.setDescription(data.description());
        }

        if (data.enable() != null) {
            if (data.enable() == false && account.isDefault()) {
                throw new CannotDeactivateDefaultAccountException();
            }
            account.setEnable(data.enable());
        }

        if (data.isDefault() != null) {
            if (!data.isDefault() && account.isDefault()) {
                throw new CannotDeactivateDefaultAccountException();
            }

            Account oldDeafaultAccount = accountGateway.findDefaultByUserId(account.getUser().getId()).orElseThrow(DefaultAccountNotFoundException::new);

            oldDeafaultAccount.setDefault(false);

            accountGateway.update(oldDeafaultAccount);

            account.setDefault(data.isDefault());
        }

        if (data.withdrawFee() != null) {
            account.setWithdrawFee(data.withdrawFee());
        }
        if (data.depositFee() != null) {
            account.setDepositFee(data.depositFee());
        }

        return accountGateway.update(account);

    }

}
