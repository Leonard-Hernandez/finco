package com.finco.finco.infrastructure.ai.gateway;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.finco.finco.entity.ai.gateway.AiGateway;
import com.finco.finco.infrastructure.account.gateway.AccountAiTools;
import com.finco.finco.infrastructure.transaction.gateway.TransactionAiTools;

@Service
public class AiGatewayImpl implements AiGateway {

    private final ChatClient chatClient;

    public AiGatewayImpl(ChatClient.Builder builder, TransactionAiTools transactionAiTools,
            AccountAiTools accountAiTools) {

        this.chatClient = builder.defaultTools(transactionAiTools, accountAiTools).build();
    }

    @Override
    public String getAnswer(String question, Long userId) {

        return chatClient.prompt().user(question + " datos: id: " + userId).system(
                "You are finco a finacial expert and you rol is help the user with their personal finance using the tools").call().content();


    }

}
