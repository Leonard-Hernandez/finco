package com.finco.finco.infrastructure.ai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.ai.gateway.AiGateway;
import com.finco.finco.entity.security.gateway.AuthGateway;

import jakarta.websocket.server.PathParam;

@RestController
public class AiController {

    private final AiGateway aiGateway;
    private final AuthGateway authGateway;

    public AiController(AiGateway aiGateway, AuthGateway authGateway) {
        this.aiGateway = aiGateway;
        this.authGateway = authGateway;
    }

    @GetMapping("/ai/answer")
    public String getAnswer(@PathParam("question") String question) {
        Long userId = authGateway.getAuthenticatedUserId();
        return aiGateway.getAnswer(question, userId);
    }

}
