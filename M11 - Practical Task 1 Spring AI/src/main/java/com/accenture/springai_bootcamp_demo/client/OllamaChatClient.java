package com.accenture.springai_bootcamp_demo.client;

import com.accenture.springai_bootcamp_demo.entity.ChatMessage;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Thin client over a local Ollama model, backed by Spring AI's {@link ChatClient}.
 * Keeps the public surface intentionally small: callers hand over the
 * conversation history and receive the assistant's reply text.
 */
@Slf4j
@Component
public class OllamaChatClient {

    private final ChatClient chatClient;
    private final String systemPrompt;

    public OllamaChatClient(ChatModel chatModel,
                             @Value("${app.ai.system-prompt}") String systemPrompt) {
        this.chatClient = ChatClient.builder(chatModel).build();
        this.systemPrompt = systemPrompt;
    }

    public String complete(List<ChatMessage> history) {
        try {
            String reply = chatClient.prompt().messages(toMessages(history)).call().content();
            return extractContent(reply);
        } catch (AiChatException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Ollama chat request failed", ex);
            throw new AiChatException("Failed to reach the local Ollama model: " + ex.getMessage(), ex);
        }
    }

    private List<Message> toMessages(List<ChatMessage> history) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(systemPrompt));
        for (ChatMessage message : history) {
            messages.add(switch (message.getRole()) {
                case SYSTEM -> new SystemMessage(message.getContent());
                case USER -> new UserMessage(message.getContent());
                case ASSISTANT -> new AssistantMessage(message.getContent());
            });
        }
        return messages;
    }

    private String extractContent(String content) {
        if (!StringUtils.hasText(content)) {
            throw new AiChatException("Ollama returned an empty response");
        }
        return content.trim();
    }
}
