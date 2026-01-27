package com.finco.finco.infrastructure.ai.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.finco.finco.infrastructure.ai.dto.AiAskDto;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
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

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/error")
    public ErrorResponse handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage(), LocalDateTime.now());
        return errorResponse;
    }

}
