package com.finco.finco.infrastructure.config.security.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.finco.finco.infrastructure.config.security.services.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;

    @Value("${frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();
        String token;

        if (principal instanceof OAuth2User oauth2User) {
            // Extract user details from OAuth2User
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            
            // Generate JWT token
            token = jwtService.generateToken(oauth2User);
            logger.info("OAuth2 login successful for user: " + email);
            
            // Build the redirect URL with the token
            String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                    .queryParam("token", token)
                    .queryParam("email", email)
                    .queryParam("name", name)
                    .build().toUriString();
            
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else {
            logger.error("Principal is not an OAuth2User");
            throw new IllegalStateException("Principal is not an OAuth2User");
        }
    }
}
