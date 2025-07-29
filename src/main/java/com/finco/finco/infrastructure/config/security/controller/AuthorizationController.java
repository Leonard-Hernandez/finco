package com.finco.finco.infrastructure.config.security.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.user.dto.UserLoginData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@Tag(name = "User", description = "User management endpoints")
public class AuthorizationController {

    @Operation(summary = "Login", description = "Login user and return a token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", 
                content = @Content(schema = @Schema(implementation = Map.class, 
                example = "{\"username\":\"user@example.com\",\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", 
                content = @Content(schema = @Schema(implementation = Map.class, 
                example = "{\"message\":\"User or password incorrect\",\"error\":\"Bad credentials\"}"))) })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginData data) {
        // is a dummy for swagger documentation
        return ResponseEntity.ok().build();
    }

}
