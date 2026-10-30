package com.finco.finco.entity.security.model;

/**
 * Scopes issued by the authorization server (authco) for the finco resource.
 */
public final class FincoScopes {

    public static final String ADMIN = "finco:admin";
    public static final String PREMIUM = "finco:premium";

    /** Authority form of the scopes, as granted by Spring Security ({@code SCOPE_} prefix). */
    public static final String SCOPE_ADMIN = "SCOPE_" + ADMIN;
    public static final String SCOPE_PREMIUM = "SCOPE_" + PREMIUM;

    private FincoScopes() {
    }

}
