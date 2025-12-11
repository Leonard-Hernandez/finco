package com.finco.finco.infrastructure.ai.gateway;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Service;

import com.finco.finco.entity.ai.gateway.AiGateway;
import com.finco.finco.infrastructure.account.gateway.AccountAiTools;
import com.finco.finco.infrastructure.transaction.gateway.TransactionAiTools;

@Service
public class AiGatewayImpl implements AiGateway {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public AiGatewayImpl(ChatClient.Builder builder, TransactionAiTools transactionAiTools,
            AccountAiTools accountAiTools) {

        this.chatMemory = MessageWindowChatMemory.builder().maxMessages(10).build();
        this.chatClient = builder.defaultTools(transactionAiTools, accountAiTools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Override
    public String getAnswer(String question, Long userId) {

        return chatClient.prompt()
                .user(question + " datos: id: " + userId)
                .system(
                        "You are finco a finacial expert and you rol is help the user with their personal finance using the tools")
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                .call().content();

    }

}
