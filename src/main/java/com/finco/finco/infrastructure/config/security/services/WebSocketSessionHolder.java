package com.finco.finco.infrastructure.config.security.services;

public final class WebSocketSessionHolder {
    private static final ThreadLocal<String> sessionId = new ThreadLocal<>();

    public static void setSessionId(String id) {
        sessionId.set(id);
    }

    public static String getSessionId() {
        return sessionId.get();
    }

    public static void clear() {
        sessionId.remove();
    }
}
