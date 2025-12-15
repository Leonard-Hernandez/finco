package com.finco.finco.infrastructure.ai.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.finco.finco.entity.ai.gateway.AiGateway;
import com.finco.finco.entity.security.gateway.AuthGateway;

@RestController
public class AiController {

    private final AiGateway aiGateway;
    private final AuthGateway authGateway;

    public AiController(AiGateway aiGateway, AuthGateway authGateway) {
        this.aiGateway = aiGateway;
        this.authGateway = authGateway;
    }

    @GetMapping("/ai/answer")
    public String getAnswer(
            @RequestParam(required = true) String message,
            @RequestParam(required = false) MultipartFile image) throws IOException {
        Long userId = authGateway.getAuthenticatedUserId();
        return aiGateway.getAnswer(message, userId, image != null ? image.getBytes() : null,
                image != null ? image.getContentType() : null);
    }

}
