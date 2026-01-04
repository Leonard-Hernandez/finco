package com.finco.finco.infrastructure.ai.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.finco.finco.infrastructure.ai.dto.AiAskDto;
import com.finco.finco.infrastructure.config.security.services.WebSocketSessionHolder;
import com.finco.finco.usecase.ai.AiGetAnswerUseCase;

@Controller
public class AiController {

    private final AiGetAnswerUseCase aiGetAnswerUseCase;
    private final SimpMessagingTemplate messagingTemplate;

    public AiController(AiGetAnswerUseCase aiGetAnswerUseCase,
            SimpMessagingTemplate messagingTemplate) {
        this.aiGetAnswerUseCase = aiGetAnswerUseCase;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping(value = "/chat")
    public void SendMessage(@Payload AiAskDto aiAskDto, Principal principal, SimpMessageHeaderAccessor headerAccessor)
            throws Exception {
        WebSocketSessionHolder.setSessionId(headerAccessor.getSessionId());

        try {
            String response = aiGetAnswerUseCase.execute(aiAskDto);
            messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/chat", response);

        } finally {
            WebSocketSessionHolder.clear();
        }

    }

}
