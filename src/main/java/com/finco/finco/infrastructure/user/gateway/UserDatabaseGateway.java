package com.finco.finco.infrastructure.user.gateway;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.config.db.mapper.UserMapper;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

@Component
public class UserDatabaseGateway implements UserGateway {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDatabaseGateway(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @LogExecution(logReturnValue = false, logArguments = false)
    public User create(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toUser(userRepository.save(userMapper.toLightUserSchema(user)));

    }

    @Override
    @LogExecution(logReturnValue = false, logArguments = false)
    public User update(User user) {
        return userMapper.toUser(userRepository.save(userMapper.toLightUserSchema(user)));
    }

    @Override
    @LogExecution(logReturnValue = false, logArguments = false)
    public User delete(User user) {
        user.setEnable(false);
        return userMapper.toUser(userRepository.save(userMapper.toLightUserSchema(user)));
    }

    @Override
    @LogExecution(logReturnValue = false, logArguments = false)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(userMapper::toUser);
    }

    @Override
    @LogExecution(logReturnValue = false, logArguments = false)
    public PagedResult<User> findAll(PageRequest pageRequest) {
        Pageable springPageable = toPageable(pageRequest);
        Page<UserSchema> userSchemaPage = userRepository.findAllByEnableTrue(springPageable);
        return userMapper.toUserPagedResult(userSchemaPage, pageRequest);
    }

}
