package com.finco.finco.infrastructure.ai.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.finco.finco.infrastructure.ai.dto.AiAskDto;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.config.security.services.WebSocketSessionHolder;
import com.finco.finco.usecase.ai.AiGetAnswerUseCase;

@Controller
public class AiController {

    private final AiGetAnswerUseCase aiGetAnswerUseCase;

    public AiController(AiGetAnswerUseCase aiGetAnswerUseCase) {
        this.aiGetAnswerUseCase = aiGetAnswerUseCase;
    }

    @MessageMapping(value = "/chat")
    @SendToUser("/queue/chat")
    public String SendMessage(@Payload AiAskDto aiAskDto, Principal principal, SimpMessageHeaderAccessor headerAccessor)
            throws Exception {
        try {
            WebSocketSessionHolder.setSessionId(headerAccessor.getSessionId());
            return aiGetAnswerUseCase.execute(aiAskDto);
        } finally {
            WebSocketSessionHolder.clear();
        }
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/error")
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse(e.getClass().getSimpleName(), e.getMessage(), LocalDateTime.now());
    }

}
