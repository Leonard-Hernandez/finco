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
import com.finco.finco.infrastructure.config.security.services.WebSocketAuthHolder;
import com.finco.finco.infrastructure.config.security.services.WebSocketSessionHolder;

@Component
public class AuthSpringSecurityGateway implements AuthGateway {

    private final UserRepository userRepository;
    private final WebSocketAuthHolder webSocketAuthHolder;

    public AuthSpringSecurityGateway(UserRepository userRepository, WebSocketAuthHolder webSocketAuthHolder) {
        this.userRepository = userRepository;
        this.webSocketAuthHolder = webSocketAuthHolder;
    }

    @Override
    @LogExecution(logReturnValue = false)
    public Long getAuthenticatedUserId() {
        Authentication authentication = getAuthentication();
        UserSchema user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
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
            String sessionId = WebSocketSessionHolder.getSessionId();
            if (sessionId != null) {
                auth = webSocketAuthHolder.get(sessionId);
            }
        }
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedBusinessException();
        }
        return auth;
    }

}
