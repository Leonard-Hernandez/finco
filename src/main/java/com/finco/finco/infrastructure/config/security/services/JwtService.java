package com.finco.finco.infrastructure.config.security.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    public static final SecretKey SECRECT_KEY = Jwts.SIG.HS256.key().build();
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CONTEND_TYPE = "application/json";

    public JwtService(UserRepository userRepository) {

    }

    public String generateToken(User user) throws JsonProcessingException {

        Collection<? extends GrantedAuthority> roles = user.getAuthorities();

        Claims claims = Jwts.claims().add(
                "authorities", new ObjectMapper().writeValueAsString(roles)).build();

        String token = Jwts.builder()
                .subject(user.getUsername())
                .claims(claims)
                .signWith(SECRECT_KEY)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 7200000))
                .compact();

        return token;

    }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(SECRECT_KEY).build().parseSignedClaims(token).getPayload();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token)
            throws StreamReadException, DatabindException, IOException {

        String authoritiesClaims = getClaims(token).get("authorities").toString();

        Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
                .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

        return authorities;
    }

    public boolean isTokenValid(String token) {
        try {

            Jwts.parser()
                    .verifyWith(SECRECT_KEY)
                    .build()
                    .parse(token);

            return true;

        } catch (JwtException ex) {
            return false;
        }
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }
}
