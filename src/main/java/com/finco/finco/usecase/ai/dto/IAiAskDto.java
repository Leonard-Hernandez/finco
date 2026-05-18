package com.finco.finco.usecase.ai.dto;

import java.util.List;

public interface IAiAskDto {

    String prompt();
    Long userId();
    List<? extends IAiAttachmentDto> attachments();

}
