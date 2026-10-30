package com.finco.finco.entity.security.model;

import java.util.List;

/**
 * Scopes issued by the authorization server (authco) for the finco resource.
 */
public final class FincoScopes {

    public static final String ADMIN = "finco:admin";
    public static final String PREMIUM = "finco:premium";

    /** Scopes clients may request. {@link #ADMIN} is granted to operators only and is not advertised. */
    public static final List<String> CLIENT_REQUESTABLE = List.of(PREMIUM);

    private FincoScopes() {
    }

}
