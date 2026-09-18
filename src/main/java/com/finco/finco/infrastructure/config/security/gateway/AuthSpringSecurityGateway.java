package com.finco.finco.infrastructure.config.security.gateway;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.infrastructure.config.authco.service.UserProvisionService;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AuthSpringSecurityGateway implements AuthGateway {

    private final UserRepository userRepository;
    private final UserProvisionService userProvisionService;

    @Override
    @LogExecution(logReturnValue = false)
    public Long getAuthenticatedUserId() {
        Authentication authentication = getAuthentication();
        String sub = authentication.getName();

        UserSchema user = userRepository.findByAuthcoUserId(sub)
                .orElseGet(() -> userProvisionService.provisionUser(sub, getToken()));
        return user.getId();
    }

    @Override
    @LogExecution()
    public boolean hasScope(String scope) {
        Authentication authentication = getAuthentication();

        String fullScopeName = "SCOPE_" + scope;
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(fullScopeName));
    }

    @Override
    @LogExecution(logReturnValue = false)
    public void verifyOwnershipOrAdmin(Long ownerId) {
        Long authenticatedUserId = getAuthenticatedUserId();

        if (!authenticatedUserId.equals(ownerId)) {
            if (!hasScope("admin")) {
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

    private String getToken() {
        Jwt jwt = (Jwt) getAuthentication().getPrincipal();
        return jwt.getTokenValue();
    }

}
