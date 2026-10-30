package com.finco.finco.infrastructure.config.authco.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.infrastructure.config.authco.dto.UserInfoDto;
import com.finco.finco.infrastructure.config.authco.gateway.AuthcoRestGateway;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserProvisionService {

    private AuthcoRestGateway authcoRestGateway;
    private UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserSchema provisionUser(String sub, String token) {

        if (sub == null) {
            throw new AccessDeniedBusinessException();
        }

        UserInfoDto infoDto = authcoRestGateway.getUserInfo(token);

        if (infoDto.email() == null || infoDto.email().isBlank()) {
            throw new AccessDeniedBusinessException();
        }

        Optional<UserSchema> optional = userRepository.findByEmail(infoDto.email());

        if (optional.isPresent()) {
            if (!infoDto.emailVerified()) {
                throw new AccessDeniedBusinessException();
            }
            UserSchema userSchema = optional.get();

            userSchema.setAuthcoUserId(sub);

            return userRepository.save(userSchema);
        }

        UserSchema newUser = new UserSchema();

        newUser.setAuthcoUserId(sub);
        newUser.setEmail(infoDto.email());
        newUser.setName(infoDto.name() != null ? infoDto.name() : infoDto.email());

        return userRepository.save(newUser);

    }

}
