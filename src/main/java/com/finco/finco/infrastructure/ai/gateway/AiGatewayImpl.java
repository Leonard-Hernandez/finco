package com.finco.finco.infrastructure.ai.gateway;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
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
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(), new MyLogAdvisor())
                .build();

        this.systemPromptTemplate = new SystemPromptTemplate(ResourceUtils.getText("classpath:prompts/system.md"));
    }

    @Override
    public String getAnswer(String question, Long userId, byte[] image, String imageExtension) {

        UserMessage userMessage = new UserMessage(question);

        if (image != null) {
            Media media = new Media(
                    MimeTypeUtils.parseMimeType(imageExtension),
                    new ByteArrayResource(image));
            userMessage = userMessage.mutate().media(media).build();
        }

        Message systemMessage = this.systemPromptTemplate.createMessage(Map.of("id", userId));

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        ChatResponse response = chatClient.prompt(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                .call().chatResponse();

        System.out.println(String.format("""
                RESPONSE: %s
                USAGE: %s
                """, response.getResult().getOutput().getText(), response.getMetadata().getUsage()));

        return response.getResult().getOutput().getText();

    }

    static class MyLogAdvisor implements BaseAdvisor {

		@Override
		public int getOrder() {
			return 0;
		}

		@Override
		public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
			chatClientRequest.prompt()
				.getInstructions()
				.stream()
				.filter(m -> m.getMessageType() == MessageType.TOOL)
				.map(m -> (ToolResponseMessage) m)
				.flatMap(m -> m.getResponses().stream())
				.forEach(r -> System.out.println("TOOL RESPONSE DATA:\n" + r.responseData()));

			return chatClientRequest;
		}

		@Override
		public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
			return chatClientResponse;
		}

	}

}
