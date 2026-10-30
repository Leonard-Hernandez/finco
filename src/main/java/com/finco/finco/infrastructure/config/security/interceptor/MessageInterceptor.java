package com.finco.finco.infrastructure.config.security.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class MessageInterceptor implements ChannelInterceptor {

    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final JwtDecoder jwtDecoder;

    public MessageInterceptor(JwtDecoder jwtDecoder) {
        this.jwtAuthenticationConverter = new JwtAuthenticationConverter();
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        var authHeaderList = accessor.getNativeHeader("Authorization");

        String authHeader = authHeaderList != null && !authHeaderList.isEmpty() ? authHeaderList.getFirst() : null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            Jwt jwt = jwtDecoder.decode(authHeader.substring(7));

            var authentication = jwtAuthenticationConverter.convert(jwt);

            accessor.setUser(authentication);

            return message;

        }

        return message;

    }

}
