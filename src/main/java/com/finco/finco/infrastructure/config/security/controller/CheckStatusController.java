package com.finco.finco.infrastructure.config.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.config.db.mapper.UserMapper;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.config.security.dto.SecurityResponseDto;
import com.finco.finco.infrastructure.config.security.services.JwtService;
import com.finco.finco.infrastructure.user.dto.UserPublicData;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Auth", description = "Authentication endpoints")
public class CheckStatusController {

    private JwtService jwtService;
    private UserRepository userRepository;
    private UserMapper userMapper;

    public CheckStatusController(JwtService jwtService, UserRepository userRepository, UserMapper userMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/auth/check-status")
    @Operation(summary = "Check status", description = "Check status of the current user")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200", description = "User token is calid", 
                content = @Content(schema = @Schema(implementation = SecurityResponseDto.class))),
            @ApiResponse(
                responseCode = "500", description = "Token not valid or authorization header not found", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<SecurityResponseDto> checkStatus(
            @Parameter(hidden = true) 
            @RequestHeader("Authorization") 
            String token) {
        token = token.replace(JwtService.PREFIX_TOKEN, "");

        if (!jwtService.isTokenValid(token)) {
            throw new JwtException("Token is invalid or expired");
        }

        String username = jwtService.getUsername(token);

        UserSchema userSchema = userRepository.findByEmail(username).get();
        UserPublicData userPublicData = new UserPublicData(userMapper.toUser(userSchema));
        return ResponseEntity.ok(new SecurityResponseDto(userPublicData, token));

    }

}
