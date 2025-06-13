package com.finco.finco.infrastructure.user.gateway;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ScrollPosition.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.config.db.mapper.UserMapper;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.RoleSchema;
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

        verifyUserAuth(user);

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
        verifyUserAuth(user);

        userRepository.delete(userMapper.toUserSchema(user));
    }

    @Override
    public Optional<User> findById(Long id) {
        verifyUserAuth(id);

        return userRepository.findById(id).map(userMapper::toUser);
    }

    public void verifyUserAuth(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        UserSchema authUser = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        
        Optional<RoleSchema> admin = authUser.getRoles().stream().filter(role -> 
            "ROLE_ADMIN".equals(role.getName())
        ).findFirst();

        if (!authUser.getId().equals(user.getId()) || !admin.isPresent()) {
            throw new UserNotFoundException();
        }
    }

    public void verifyUserAuth(Long id) {
        User user = userMapper.toLigthUser(userRepository.findById(id).orElseThrow(UserNotFoundException::new));

        verifyUserAuth(user);
    }

    @Override
    public PagedResult<User> findAll(PageRequest pageRequest) {
        Sort sort = pageRequest.getSortBy()
        .map(sortBy -> {
            Sort.Direction direction = pageRequest.getSortDirection()
                                                .filter(d -> d.equalsIgnoreCase("desc"))
                                                .map(d -> Sort.Direction.DESC)
                                                .orElse(Sort.Direction.ASC);
            return Sort.by(direction, sortBy);
        })
        .orElse(Sort.unsorted());

        Pageable springPageable = org.springframework.data.domain.PageRequest.of(
        pageRequest.getPageNumber(),
        pageRequest.getPageSize(),
        sort
        );

        Page<UserSchema> userSchemaPage = userRepository.findAll(springPageable);

        return userMapper.toUserPagedResult(userSchemaPage, pageRequest); // Nuevo método en mapper

    }

}
