package com.finco.finco.infrastructure.config.authco.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserInfoDto(
        String sub,
        String email,
        @JsonProperty("email_verified") boolean emailVerified,
        String name) {
}
