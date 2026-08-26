package com.finco.finco.infrastructure.ai.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.finco.finco.infrastructure.ai.dto.AiAskDto;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.usecase.ai.AiGetAnswerUseCase;

@Controller
public class AiController {

    private final AiGetAnswerUseCase aiGetAnswerUseCase;

    public AiController(AiGetAnswerUseCase aiGetAnswerUseCase) {
        this.aiGetAnswerUseCase = aiGetAnswerUseCase;
    }

    @MessageMapping(value = "/chat")
    @SendToUser("/queue/chat")
    public String SendMessage(@Payload AiAskDto aiAskDto)
            throws Exception {
            return aiGetAnswerUseCase.execute(aiAskDto);
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/error")
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse(e.getClass().getSimpleName(), e.getMessage(), LocalDateTime.now());
    }

}
