package com.finco.finco.infrastructure.config.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.infrastructure.config.db.mapper.UserMapper;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;
import com.finco.finco.infrastructure.config.security.dto.SecurityResponseDto;
import com.finco.finco.infrastructure.config.security.services.JwtService;
import com.finco.finco.infrastructure.user.dto.UserPublicData;

@RestController
public class CheckStatusController {

    private JwtService jwtService;
    private UserRepository  userRepository;
    private UserMapper userMapper;

    public CheckStatusController(JwtService jwtService, UserRepository userRepository, UserMapper userMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/auth/check-status")
    public ResponseEntity<SecurityResponseDto> checkStatus() {

        String token =(String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        if (!jwtService.isTokenValid(token) || jwtService.isTokenExpired(token)) {
            throw new AccessDeniedBusinessException();
        }

        String username = jwtService.getClaims(token).getSubject();

        UserSchema userSchema = userRepository.findByEmail(username).get();
        UserPublicData userPublicData = new UserPublicData(userMapper.toUser(userSchema));
        return ResponseEntity.ok(new SecurityResponseDto(userPublicData, token));
        
    }

}
