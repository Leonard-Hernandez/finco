package com.finco.finco.infrastructure.config.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finco.finco.infrastructure.config.db.mapper.UserMapper;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;
import com.finco.finco.infrastructure.config.security.dto.SecurityResponseDto;
import com.finco.finco.infrastructure.config.security.services.JwtService;
import com.finco.finco.infrastructure.user.dto.UserLoginData;
import com.finco.finco.infrastructure.user.dto.UserPublicData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Auth", description = "Authentication endpoints")
public class LoginController {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UserRepository userRepository;
    private UserMapper userMapper;

    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService,
            UserRepository userRepository, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Login", description = "Login user and return a token JWT")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200", description = "Login successful", 
                content = @Content(schema = @Schema(implementation = SecurityResponseDto.class))),
            @ApiResponse(
                responseCode = "401", description = "Invalid credentials", 
                content = @Content(schema = @Schema(implementation = SecurityResponseDto.class)))
                        }
                )
    @PostMapping("/auth/login")
    public ResponseEntity<SecurityResponseDto> login(@RequestBody UserLoginData data) {
        
        Authentication authToken = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        Authentication authentication = authenticationManager.authenticate(authToken);
        String token = null;

        try {
            token = jwtService.generateToken((User) authentication.getPrincipal());
        } catch (JsonProcessingException e) {
            
        }

        UserSchema userSchema = userRepository.findByEmail(data.username()).get();
        UserPublicData userPublicData = new UserPublicData(userMapper.toUser(userSchema));
        return ResponseEntity.ok(new SecurityResponseDto(userPublicData, token));

    }

}
