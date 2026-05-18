package com.finco.finco.infrastructure.ai.gateway;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.util.ResourceUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.finco.finco.entity.ai.gateway.AiGateway;
import com.finco.finco.infrastructure.config.ai.DelegatorToolCallbackProvider;
import com.finco.finco.usecase.ai.dto.IAiAskDto;
import com.finco.finco.usecase.ai.dto.IAiAttachmentDto;

@Service
public class AiGatewayImpl implements AiGateway {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final String systemPromptTemplate;

    public AiGatewayImpl(ChatClient.Builder builder, ToolCallbackProvider provider,
            JdbcChatMemoryRepository chatMemoryRepository) {

        DelegatorToolCallbackProvider delegatorToolCallbackProvider = new DelegatorToolCallbackProvider(provider);

        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(20)
                .build();

        this.chatClient = builder
                .defaultToolCallbacks(delegatorToolCallbackProvider)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();

        this.systemPromptTemplate = ResourceUtils.getText("classpath:prompts/system.md");
    }

    @Override
    public String getAnswer(IAiAskDto aiAskDto) {

        UserMessage userMessage = new UserMessage(aiAskDto.prompt());

        List<? extends IAiAttachmentDto> attachments = aiAskDto.attachments();
        if (attachments != null && !attachments.isEmpty()) {
            List<Media> mediaList = attachments.stream()
                    .map(att -> new Media(
                            MimeTypeUtils.parseMimeType(att.mimeType()),
                            new ByteArrayResource(Base64.getDecoder().decode(att.data()))))
                    .toList();
            userMessage = userMessage.mutate().media(mediaList).build();
        }

        return chatClient.prompt()
                .system(s -> s.text(systemPromptTemplate).params(Map.of("id", String.valueOf(aiAskDto.userId()))))
                .messages(userMessage)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, String.valueOf(aiAskDto.userId())))
                .call()
                .content();
    }

}
