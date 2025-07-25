package com.finco.finco.infrastructure.config.security.gateway;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

@Component
public class AuthSpringSecurityGateway implements AuthGateway {

    private final UserRepository userRepository;

    public AuthSpringSecurityGateway(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @LogExecution(logReturnValue = false)
    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedBusinessException();
        }

        UserSchema authenticatedUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        return authenticatedUser.getId();
    }

    @Override
    @LogExecution()
    public boolean isAuthenticatedUserInRole(String roleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

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

}
