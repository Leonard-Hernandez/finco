package com.finco.finco.entity.security.gateway;

public interface AuthGateway {

    Long getAuthenticatedUserId();

    boolean isAuthenticatedUserInRole(String roleName);

    void verifyOwnershipOrAdmin(Long ownerId);

}
