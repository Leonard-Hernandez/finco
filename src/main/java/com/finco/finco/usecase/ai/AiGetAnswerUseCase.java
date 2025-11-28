package com.finco.finco.usecase.ai;

import com.finco.finco.entity.ai.gateway.AiGateway;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class AiGetAnswerUseCase {

    private AuthGateway authGateway;
    private AiGateway aiGateway;

    public AiGetAnswerUseCase(AuthGateway authGateway, AiGateway aiGateway) {
        this.authGateway = authGateway;
        this.aiGateway = aiGateway;
    }

    public String execute(String question, Long userId) {
        authGateway.verifyOwnershipOrAdmin(userId);

        return aiGateway.getAnswer(question, userId);
    }

}
