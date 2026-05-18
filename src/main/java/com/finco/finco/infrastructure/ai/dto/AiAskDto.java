package com.finco.finco.infrastructure.ai.dto;

import java.util.List;

import com.finco.finco.usecase.ai.dto.IAiAskDto;

public record AiAskDto(String prompt, Long userId, List<AiAttachmentDto> attachments) implements IAiAskDto {

    public AiAskDto {
        if (attachments == null) {
            attachments = List.of();
        }
    }

}
