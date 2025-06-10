package com.finco.finco.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.finco.finco.infrastructure.config.db.mapper.RoleMapper;
import com.finco.finco.infrastructure.config.db.mapper.UserMapper;
import com.finco.finco.infrastructure.config.db.repository.RoleRepository;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.role.gateway.RoleDatabaseGateway;
import com.finco.finco.infrastructure.user.gateway.UserDatabaseGateway;
import com.finco.finco.usecase.user.CreateUserUseCase;
import com.finco.finco.usecase.user.DeleteUserUseCase;
import com.finco.finco.usecase.user.GetUserUseCase;
import com.finco.finco.usecase.user.UpdateUserUseCase;

@Configuration
public class AppConfig {

    @Bean
    CreateUserUseCase createUserUserCase(UserRepository userRepository, RoleRepository roleRepository,
            UserMapper userMapper, RoleMapper roleMapper, PasswordEncoder passwordEncoder) {
        UserDatabaseGateway userGateway = new UserDatabaseGateway(userRepository, userMapper, passwordEncoder);
        RoleDatabaseGateway roleGateway = new RoleDatabaseGateway(roleRepository, roleMapper);
        return new CreateUserUseCase(userGateway, roleGateway);
    }

    @Bean
    UpdateUserUseCase updateUserUseCase(UserRepository userRepository, UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        UserDatabaseGateway userGateway = new UserDatabaseGateway(userRepository, userMapper, passwordEncoder);
        return new UpdateUserUseCase(userGateway);
    }

    @Bean
    GetUserUseCase getUserUseCase(UserRepository userRepository, UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        UserDatabaseGateway userGateway = new UserDatabaseGateway(userRepository, userMapper, passwordEncoder);
        return new GetUserUseCase(userGateway);
    }

    @Bean
    DeleteUserUseCase deleteUserUseCase(UserRepository userRepository, UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        UserDatabaseGateway userGateway = new UserDatabaseGateway(userRepository, userMapper, passwordEncoder);
        return new DeleteUserUseCase(userGateway);
    }

}
