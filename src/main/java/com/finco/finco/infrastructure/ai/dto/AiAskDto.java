package com.finco.finco.infrastructure.ai.dto;

import com.finco.finco.usecase.ai.dto.IAiAskDto;

public record AiAskDto(String question, Long userId) implements IAiAskDto {

}
