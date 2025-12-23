package com.finco.finco.entity.ai.gateway;

import com.finco.finco.usecase.ai.dto.IAiAskDto;

public interface AiGateway {

    String getAnswer(IAiAskDto aiAskDto);

}
