package com.finco.finco.usecase.ai;

import com.finco.finco.entity.ai.gateway.AiGateway;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.usecase.ai.dto.IAiAskDto;

public class AiGetAnswerUseCase {

    private AuthGateway authGateway;
    private AiGateway aiGateway;

    public AiGetAnswerUseCase(AuthGateway authGateway, AiGateway aiGateway) {
        this.authGateway = authGateway;
        this.aiGateway = aiGateway;
    }

    public String execute(IAiAskDto aiAskDto) {
        authGateway.verifyOwnershipOrAdmin(aiAskDto.userId());
        return aiGateway.getAnswer(aiAskDto);
    }

}
