package com.finco.finco.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.PlatformTransactionManager;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.role.gateway.RoleGateway;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.infrastructure.config.aop.TransactionalAspect;
import com.finco.finco.usecase.account.CreateAccountUseCase;
import com.finco.finco.usecase.account.UpdateAccountUseCase;
import com.finco.finco.usecase.user.CreateUserAdminUseCase;
import com.finco.finco.usecase.user.CreateUserUseCase;
import com.finco.finco.usecase.user.DeleteUserUseCase;
import com.finco.finco.usecase.user.GetAllUserUseCase;
import com.finco.finco.usecase.user.GetUserUseCase;
import com.finco.finco.usecase.user.UpdateUserUseCase;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    // aop

    @Bean
    TransactionalAspect transactionalAspect(PlatformTransactionManager transactionManager) {
        return new TransactionalAspect(transactionManager);
    }

    // User Beans

    @Bean
    CreateUserUseCase createUserUserCase(UserGateway userGateway, RoleGateway roleGateway) {
        return new CreateUserUseCase(userGateway, roleGateway);
    }

    @Bean
    CreateUserAdminUseCase createUserAdminUserCase(UserGateway userGateway, RoleGateway roleGateway, AuthGateway authGateway) {
        return new CreateUserAdminUseCase(userGateway, roleGateway, authGateway);
    }

    @Bean
    UpdateUserUseCase updateUserUseCase(UserGateway userGateway, AuthGateway authGateway) {
        return new UpdateUserUseCase(userGateway, authGateway);
    }

    @Bean
    GetUserUseCase getUserUseCase(UserGateway userGateway, AuthGateway authGateway) {
        return new GetUserUseCase(userGateway, authGateway);
    }

    @Bean
    GetAllUserUseCase getAllUserUseCase(UserGateway userGateway, AuthGateway authGateway) {
        return new GetAllUserUseCase(userGateway, authGateway);
    }

    @Bean
    DeleteUserUseCase deleteUserUseCase(UserGateway userGateway, AuthGateway authGateway) {
        return new DeleteUserUseCase(userGateway, authGateway);
    }

    // Accounts beans

    @Bean
    CreateAccountUseCase createAccountUseCase(UserGateway userGateway, AccountGateway accountGateway, AuthGateway authGateway) {
        return new CreateAccountUseCase(accountGateway, userGateway, authGateway);
    }

    @Bean
    UpdateAccountUseCase UpdateAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        return new UpdateAccountUseCase(accountGateway, authGateway);
    }

}
