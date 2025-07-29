package com.finco.finco.infrastructure.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User login data")
public record UserLoginData(
    @Schema(description = "User name") 
    String username,
    @Schema(description = "User password") 
    String password) {
}
