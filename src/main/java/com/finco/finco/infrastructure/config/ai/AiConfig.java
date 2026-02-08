package com.finco.finco.infrastructure.config.ai;

import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.finco.finco.infrastructure.account.gateway.AccountAiTools;
import com.finco.finco.infrastructure.transaction.gateway.TransactionAiTools;

@Configuration
public class AiConfig {

    @Bean
    MethodToolCallbackProvider tools(AccountAiTools accountAiTools, TransactionAiTools transactionAiTools) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(accountAiTools, transactionAiTools)
                .build();
    }

}
