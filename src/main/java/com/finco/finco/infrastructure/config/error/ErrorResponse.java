package com.finco.finco.infrastructure.config.error;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponse(
    @Schema(description = "Error name", example = "Error name") 
    String error,
    @Schema(description = "Error message", example = "Error message") 
    String message,
    @Schema(description = "Error datetime", example = "2025-07-28T21:58:28.262") 
    LocalDateTime datetime) {

}
