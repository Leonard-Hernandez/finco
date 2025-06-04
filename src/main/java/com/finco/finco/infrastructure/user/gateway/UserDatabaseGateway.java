package com.finco.finco.infrastructure.user.gateway;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.config.db.mapper.UserMapper;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

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
    public User create(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toUser(userRepository.save(userMapper.toUserSchema(user)));

    }

    @Override
    public User update(User user) {
        
        verifyUserAuth(user);  

        return userMapper.toUser(userRepository.save(userMapper.toUserSchema(user)));
    }

    @Override
    public void delete(User user) {
        userRepository.delete(userMapper.toUserSchema(user));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(userMapper::toUser);
    }

    public void verifyUserAuth(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        UserSchema authUser = userRepository.findByEmail(email).get();

        if (!authUser.getId().equals(user.getId())) {
            throw new RuntimeException("User not found");
        }
    }

}
