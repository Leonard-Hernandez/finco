package com.finco.finco.infrastructure.config.security.gateway;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.infrastructure.config.authco.dto.UserInfoDto;
import com.finco.finco.infrastructure.config.authco.gateway.AuthcoRestGateway;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AuthSpringSecurityGateway implements AuthGateway {

    private final UserRepository userRepository;
    private final AuthcoRestGateway authcoRestGateway;

    @Override
    @Transactional
    @LogExecution(logReturnValue = false)
    public Long getAuthenticatedUserId() {
        Authentication authentication = getAuthentication();
        String sub = authentication.getName();

        UserSchema user = userRepository.findByAuthcoUserId(sub)
                .orElseGet(() -> provisionUser(sub));
        return user.getId();
    }

    @Override
    @LogExecution()
    public boolean isAuthenticatedUserInRole(String roleName) {
        Authentication authentication = getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String fullRoleName = "ROLE_" + roleName.toUpperCase();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(fullRoleName));
    }

    @Override
    @LogExecution(logReturnValue = false)
    public void verifyOwnershipOrAdmin(Long ownerId) {
        Long authenticatedUserId = getAuthenticatedUserId();

        if (!authenticatedUserId.equals(ownerId)) {
            if (!isAuthenticatedUserInRole("ADMIN")) {
                throw new AccessDeniedBusinessException();
            }
        }
    }

    private Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedBusinessException();
        }
        return auth;
    }

    private UserSchema provisionUser(String sub) {

        if (sub == null) {
            throw new AccessDeniedBusinessException();
        }

        Authentication authentication = getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        UserInfoDto infoDto = authcoRestGateway.getUserInfo(jwt.getTokenValue());

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
