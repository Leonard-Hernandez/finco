package com.finco.finco.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.role.gateway.RoleGateway;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.usecase.account.CreateAccountUseCase;
import com.finco.finco.usecase.user.CreateUserAdminUseCase;
import com.finco.finco.usecase.user.CreateUserUseCase;
import com.finco.finco.usecase.user.DeleteUserUseCase;
import com.finco.finco.usecase.user.GetAllUserUseCase;
import com.finco.finco.usecase.user.GetUserUseCase;
import com.finco.finco.usecase.user.UpdateUserUseCase;

@Configuration
public class AppConfig {

    // User Beans

    @Bean
    CreateUserUseCase createUserUserCase(UserGateway userGateway, RoleGateway roleGateway) {
        return new CreateUserUseCase(userGateway, roleGateway);
    }

    @Bean
    CreateUserAdminUseCase createUserAdminUserCase(UserGateway userGateway, RoleGateway roleGateway) {
        return new CreateUserAdminUseCase(userGateway, roleGateway);
    }

    @Bean
    UpdateUserUseCase updateUserUseCase(UserGateway userGateway) {
        return new UpdateUserUseCase(userGateway);
    }

    @Bean
    GetUserUseCase getUserUseCase(UserGateway userGateway) {
        return new GetUserUseCase(userGateway);
    }

    @Bean
    GetAllUserUseCase getAllUserUseCase(UserGateway userGateway) {
        return new GetAllUserUseCase(userGateway);
    }

    @Bean
    DeleteUserUseCase deleteUserUseCase(UserGateway userGateway) {
        return new DeleteUserUseCase(userGateway);
    }

    // Accounts beans

    @Bean
    CreateAccountUseCase createAccountUseCase(UserGateway userGateway, AccountGateway accountGateway) {
        return new CreateAccountUseCase(accountGateway, userGateway);
    }

}
