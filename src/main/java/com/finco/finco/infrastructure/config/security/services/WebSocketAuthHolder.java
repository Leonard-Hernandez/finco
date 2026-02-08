package com.finco.finco.infrastructure.config.security.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthHolder {

    private final Map<String, Authentication> sessionAuth = new ConcurrentHashMap<>();

    public void store(String sessionId, Authentication auth) {
        sessionAuth.put(sessionId, auth);
    }

    public Authentication get(String sessionId) {
        return sessionAuth.get(sessionId);
    }

    public void remove(String sessionId) {
        sessionAuth.remove(sessionId);
    }

}
