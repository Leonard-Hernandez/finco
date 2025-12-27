package com.finco.finco.infrastructure.ai.controller;

import java.io.IOException;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.infrastructure.ai.dto.AiAskDto;
import com.finco.finco.usecase.ai.AiGetAnswerUseCase;

@RestController
public class AiController {

    private final AiGetAnswerUseCase aiGetAnswerUseCase;
    private final AuthGateway authGateway;
    private final SimpMessagingTemplate messagingTemplate;

    public AiController( AiGetAnswerUseCase aiGetAnswerUseCase, AuthGateway authGateway, SimpMessagingTemplate messagingTemplate) {
        this.aiGetAnswerUseCase = aiGetAnswerUseCase;
        this.authGateway = authGateway;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/ai/answer")
    public String getAnswer(
            @RequestParam(required = true) String message,
            @RequestParam(required = false) MultipartFile image) throws IOException {
        Long userId = authGateway.getAuthenticatedUserId();
        AiAskDto aiAskDto = new AiAskDto(message, userId, image.getBytes(), image.getContentType());
        return aiGetAnswerUseCase.execute(aiAskDto);
    }

    @MessageMapping(value = "/chat")
    public void SendMessage(@Payload AiAskDto aiAskDto) throws Exception {
        String response = aiGetAnswerUseCase.execute(aiAskDto);
        messagingTemplate.convertAndSendToUser(aiAskDto.userId().toString(), "/queue/chat", response);
    }

}
