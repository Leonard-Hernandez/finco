package com.finco.finco.infrastructure.config.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finco.finco.infrastructure.user.dto.UserLoginData;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.finco.finco.infrastructure.config.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        UserLoginData user = null;
        String username = null, password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), UserLoginData.class);
            username = user.username();
            password = user.password();
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        return this.authenticationManager.authenticate(token);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
        User user = (User) authResult.getPrincipal();

        Collection<? extends GrantedAuthority> roles = user.getAuthorities();

        Claims claims = Jwts.claims().add(
            "authorities", new ObjectMapper().writeValueAsString(roles)
            ).build();

        String token = Jwts.builder()
                .subject(user.getUsername())
                .claims(claims)
                .signWith(SECRECT_KEY)
                .issuedAt(new Date())
                .issuedAt(new Date(System.currentTimeMillis() + 3600000))
                .compact();

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
        
        Map<String, Object> body = new HashMap<>();
        body.put("Username", user.getUsername());
        body.put("token", token);

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTEND_TYPE);
        response.setStatus(HttpStatus.OK.value());

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        
        Map<String, Object> body = new HashMap<>();
        body.put("message", "User or password incorrect");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTEND_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

    }

    

}
