package com.finco.finco.infrastructure.config.security;

/**
 * Maps domain scopes to the authorities Spring Security grants from the JWT {@code scope} claim.
 */
public final class SecurityAuthorities {

    private static final String SCOPE_PREFIX = "SCOPE_";

    private SecurityAuthorities() {
    }

    public static String scope(String scope) {
        return SCOPE_PREFIX + scope;
    }

}
