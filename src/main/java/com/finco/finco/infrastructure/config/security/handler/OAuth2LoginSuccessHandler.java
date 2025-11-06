package com.finco.finco.infrastructure.config.security.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.finco.finco.infrastructure.config.security.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

        String token;

        if (principal instanceof OAuth2User user) {
            token = jwtService.generateToken(user);
            System.out.println("Token generado: " + token);
        } else {
            throw new IllegalArgumentException("Principal no es un OAuth2User");
        }

        String targetUrl = determineTargetUrl(request, response, authentication);

        // 6. Construir la URL final añadiendo el token como parámetro de consulta
        String finalRedirectUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, finalRedirectUrl);

    }

}
