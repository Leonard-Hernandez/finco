package com.finco.finco.entity.security.gateway;

public interface AuthGateway {

    Long getAuthenticatedUserId();

    boolean hasScope(String scope);

    void verifyOwnershipOrAdmin(Long ownerId);

}
