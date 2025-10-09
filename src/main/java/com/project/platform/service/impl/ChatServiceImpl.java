package com.project.platform.service.impl;


import com.project.platform.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author skm1229
 * @since 2025/8/12
 */
@Service
public class ChatServiceImpl implements ChatService {

    private ChatClient chatClient;

    private String prompt = "你是一个非常聪明的人工智能助手,你的名字叫km";


    public ChatServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String chat(String message) {
        return chatClient.prompt(prompt).user(message).call().content();
    }

    @Override
    public Flux<ChatResponse> streamResponse(String message) {
        return chatClient.prompt(prompt).user(message).stream().chatResponse();
    }

    @Override
    public Flux<String> streamStr(String message) {
        return chatClient.prompt(prompt).user(message).stream().content();
    }
}
