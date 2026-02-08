package com.finco.finco.infrastructure.ai.gateway;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.content.Media;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.util.ResourceUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.finco.finco.entity.ai.gateway.AiGateway;
import com.finco.finco.infrastructure.config.ai.DelegatorToolCallbackProvider;
import com.finco.finco.usecase.ai.dto.IAiAskDto;

@Service
public class AiGatewayImpl implements AiGateway {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private SystemPromptTemplate systemPromptTemplate;

    public AiGatewayImpl(ChatClient.Builder builder, ToolCallbackProvider provider,
            JdbcChatMemoryRepository chatMemoryRepository) {

        DelegatorToolCallbackProvider delegatorToolCallbackProvider = new DelegatorToolCallbackProvider(provider);

        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10)
                .build();

        this.chatClient = builder
                .defaultToolCallbacks(delegatorToolCallbackProvider)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();

        this.systemPromptTemplate = new SystemPromptTemplate(ResourceUtils.getText("classpath:prompts/system.md"));
    }

    @Override
    public String getAnswer(IAiAskDto aiAskDto) {

        UserMessage userMessage = new UserMessage(aiAskDto.prompt());

        if (aiAskDto.image() != null) {
            Media media = new Media(
                    MimeTypeUtils.parseMimeType(aiAskDto.imageExtension()),
                    new ByteArrayResource(Base64.getDecoder().decode(aiAskDto.image())));
            userMessage = userMessage.mutate().media(media).build();
        }

        Message systemMessage = this.systemPromptTemplate.createMessage(Map.of("id", aiAskDto.userId()));

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        return chatClient.prompt(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, aiAskDto.userId()))
                .call().content();
    }

}
