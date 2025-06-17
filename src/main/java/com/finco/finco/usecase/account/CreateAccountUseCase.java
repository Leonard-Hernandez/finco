package com.finco.finco.usecase.account;

import java.time.LocalDateTime;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.account.dto.IAccountRegistrationData;

public class CreateAccountUseCase {

    private AccountGateway accountGateway;
    private UserGateway userGateway;
    private AuthGateway authGateway;

    public CreateAccountUseCase(AccountGateway accountGateway, UserGateway userGateway, AuthGateway authGateway) {
        this.accountGateway = accountGateway;
        this.userGateway = userGateway;
        this.authGateway = authGateway;
    }

    public Account execute(IAccountRegistrationData data) {

        authGateway.verifyOwnershipOrAdmin(data.userId());

        User user = userGateway.findById(data.userId()).orElseThrow(UserNotFoundException::new);

        Account account = new Account();
        account.setUser(user);
        account.setName(data.name());
        account.setType(data.type());
        account.setBalance(data.balance());
        account.setCurrency(data.currency());
        account.setCreationDate(LocalDateTime.now());
        account.setDescription(data.description());

        if (user.getAccounts().size() == 0) {
            account.setDefault(true);
        }

        return accountGateway.create(account);

    }

}
