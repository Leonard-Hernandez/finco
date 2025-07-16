package com.finco.finco.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.PlatformTransactionManager;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goalAccountBalance.gateway.GoalAccountBalanceGateway;
import com.finco.finco.entity.role.gateway.RoleGateway;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.infrastructure.config.aop.TransactionalAspect;
import com.finco.finco.usecase.account.CreateAccountUseCase;
import com.finco.finco.usecase.account.DeleteAccountUseCase;
import com.finco.finco.usecase.account.DepositAccountUseCase;
import com.finco.finco.usecase.account.GetAccountUseCase;
import com.finco.finco.usecase.account.GetAllAccountUseCase;
import com.finco.finco.usecase.account.GetAllAccountsByUserUseCase;
import com.finco.finco.usecase.account.TransferAccountUseCase;
import com.finco.finco.usecase.account.UpdateAccountUseCase;
import com.finco.finco.usecase.account.WithDrawAccountUseCase;
import com.finco.finco.usecase.goal.CreateGoalUseCase;
import com.finco.finco.usecase.goal.DeleteGoalUseCase;
import com.finco.finco.usecase.goal.DepositGoalUseCase;
import com.finco.finco.usecase.goal.GetGoalUseCase;
import com.finco.finco.usecase.goal.UpdateGoalUseCase;
import com.finco.finco.usecase.goal.WithDrawGoalUseCase;
import com.finco.finco.usecase.transaction.GetCategoriesByUserUseCase;
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
    CreateUserAdminUseCase createUserAdminUserCase(UserGateway userGateway, RoleGateway roleGateway,
            AuthGateway authGateway) {
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
    CreateAccountUseCase createAccountUseCase(UserGateway userGateway, AccountGateway accountGateway,
            AuthGateway authGateway) {
        return new CreateAccountUseCase(accountGateway, userGateway, authGateway);
    }

    @Bean
    UpdateAccountUseCase UpdateAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        return new UpdateAccountUseCase(accountGateway, authGateway);
    }

    @Bean
    GetAccountUseCase getAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        return new GetAccountUseCase(accountGateway, authGateway);
    }

    @Bean
    GetAllAccountUseCase getAllAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        return new GetAllAccountUseCase(accountGateway, authGateway);
    }

    @Bean
    GetAllAccountsByUserUseCase getAllAccountsByUserUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        return new GetAllAccountsByUserUseCase(accountGateway, authGateway);
    }

    @Bean
    DepositAccountUseCase depositAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway,
            TransactionGateway transactionGateway) {
        return new DepositAccountUseCase(accountGateway, authGateway, transactionGateway);
    }

    @Bean
    WithDrawAccountUseCase withDrawAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway,
            TransactionGateway transactionGateway) {
        return new WithDrawAccountUseCase(accountGateway, authGateway, transactionGateway);
    }

    @Bean
    TransferAccountUseCase transferAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway,
            TransactionGateway transactionGateway) {
        return new TransferAccountUseCase(accountGateway, authGateway, transactionGateway);
    }

    @Bean
    DeleteAccountUseCase deleteAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        return new DeleteAccountUseCase(accountGateway, authGateway);
    }

    // Transactions beans

    @Bean
    GetCategoriesByUserUseCase getCategoriesByUserUseCase(TransactionGateway transactionGateway,
            AuthGateway authGateway) {
        return new GetCategoriesByUserUseCase(transactionGateway, authGateway);
    }

    // goal beans

    @Bean
    CreateGoalUseCase createGoalUseCase(GoalGateway goalGateway, UserGateway userGateway, AuthGateway authGateway) {
        return new CreateGoalUseCase(goalGateway, userGateway, authGateway);
    }

    @Bean
    UpdateGoalUseCase updateGoalUseCase(GoalGateway goalGateway, AuthGateway authGateway) {
        return new UpdateGoalUseCase(goalGateway, authGateway);
    }

    @Bean
    GetGoalUseCase getGoalUseCase(GoalGateway goalGateway, AuthGateway authGateway) {
        return new GetGoalUseCase(goalGateway, authGateway);
    }

    @Bean
    DeleteGoalUseCase deleteGoalUseCase(GoalGateway goalGateway, AuthGateway authGateway) {
        return new DeleteGoalUseCase(goalGateway, authGateway);
    }

    @Bean
    DepositGoalUseCase depositGoalUseCase(GoalGateway goalGateway, GoalAccountBalanceGateway goalAccountBalanceGateway,
            AccountGateway accountGateway, AuthGateway authGateway, TransactionGateway transactionGateway) {
        return new DepositGoalUseCase(goalGateway, goalAccountBalanceGateway, transactionGateway, authGateway,
                accountGateway);
    }

    @Bean
    WithDrawGoalUseCase withDrawGoalUseCase(GoalGateway goalGateway,
            GoalAccountBalanceGateway goalAccountBalanceGateway, AccountGateway accountGateway, AuthGateway authGateway,
            TransactionGateway transactionGateway) {
        return new WithDrawGoalUseCase(goalGateway, goalAccountBalanceGateway, transactionGateway, authGateway,
                accountGateway);
    }

}
