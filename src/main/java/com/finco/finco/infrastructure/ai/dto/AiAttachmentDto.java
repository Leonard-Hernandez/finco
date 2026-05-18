package com.finco.finco.infrastructure.ai.dto;

import com.finco.finco.usecase.ai.dto.IAiAttachmentDto;

public record AiAttachmentDto(String data, String mimeType) implements IAiAttachmentDto {
}
