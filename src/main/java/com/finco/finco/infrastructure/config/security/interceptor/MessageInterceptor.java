package com.finco.finco.infrastructure.config.security.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.finco.finco.infrastructure.config.security.services.JpaUserDetailsService;
import com.finco.finco.infrastructure.config.security.services.JwtService;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MessageInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final JpaUserDetailsService jpaUserDetailsService;

    public MessageInterceptor(JwtService jwtService, JpaUserDetailsService jpaUserDetailsService) {
        this.jwtService = jwtService;
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            var authHeaderList = accessor.getNativeHeader("Authorization");

            assert authHeaderList != null;
            String authHeader = authHeaderList.get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                String jwt = authHeader.replace(JwtService.PREFIX_TOKEN, "");
                String username = jwtService.getUsername(jwt);
                UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                accessor.setUser(authenticatedUser);
            }
        }

        return message;
    }

}
