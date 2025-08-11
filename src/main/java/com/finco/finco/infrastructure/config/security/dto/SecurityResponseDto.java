package com.finco.finco.infrastructure.config.security.dto;

import com.finco.finco.infrastructure.user.dto.UserPublicData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Security response")
public record SecurityResponseDto(
        @Schema(description = "User data") UserPublicData user,
        @Schema(description = "Token JWT") String token) {

}
