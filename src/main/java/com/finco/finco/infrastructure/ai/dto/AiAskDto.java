package com.finco.finco.infrastructure.ai.dto;

import com.finco.finco.usecase.ai.dto.IAiAskDto;

public record AiAskDto(String prompt, Long userId, byte[] image, String imageExtension) implements IAiAskDto {

}
