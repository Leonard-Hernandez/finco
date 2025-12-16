package com.finco.finco.infrastructure.ai.gateway;

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
import org.springframework.ai.util.ResourceUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.finco.finco.entity.ai.gateway.AiGateway;
import com.finco.finco.infrastructure.account.gateway.AccountAiTools;
import com.finco.finco.infrastructure.transaction.gateway.TransactionAiTools;

@Service
public class AiGatewayImpl implements AiGateway {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private SystemPromptTemplate systemPromptTemplate;

    public AiGatewayImpl(ChatClient.Builder builder, TransactionAiTools transactionAiTools,
            AccountAiTools accountAiTools, JdbcChatMemoryRepository chatMemoryRepository) {

        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10)
                .build();
        this.chatClient = builder
                .defaultTools(transactionAiTools, accountAiTools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();

        this.systemPromptTemplate = new SystemPromptTemplate(ResourceUtils.getText("classpath:prompts/system.md"));
    }

    @Override
    public String getAnswer(String question, Long userId, byte[] image, String imageExtension) {

        UserMessage userMessage = new UserMessage(question);

        if (image != null) {
            Media media = new Media(
                MimeTypeUtils.parseMimeType(imageExtension), 
                new ByteArrayResource(image)
            );
            userMessage = userMessage.mutate().media(media).build();
        }

        Message systemMessage = this.systemPromptTemplate.createMessage(Map.of("id", userId));

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        return chatClient.prompt(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                .call().content();

    }

}
