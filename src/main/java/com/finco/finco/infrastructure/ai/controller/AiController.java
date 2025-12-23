package com.finco.finco.infrastructure.ai.controller;

import java.io.IOException;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.finco.finco.entity.ai.gateway.AiGateway;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.infrastructure.ai.dto.AiAskDto;

@RestController
public class AiController {

    private final AiGateway aiGateway;
    private final AuthGateway authGateway;
    private final SimpMessagingTemplate messagingTemplate;

    public AiController(AiGateway aiGateway, AuthGateway authGateway, SimpMessagingTemplate messagingTemplate) {
        this.aiGateway = aiGateway;
        this.authGateway = authGateway;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/ai/answer")
    public String getAnswer(
            @RequestParam(required = true) String message,
            @RequestParam(required = false) MultipartFile image) throws IOException {
        Long userId = authGateway.getAuthenticatedUserId();
        AiAskDto aiAskDto = new AiAskDto(message, userId, image.getBytes(), image.getContentType());
        return aiGateway.getAnswer(aiAskDto);
    }

    @MessageMapping(value="/chat")
    public void SendMessage(@Payload Message message) throws Exception {
        
        
    }

}
