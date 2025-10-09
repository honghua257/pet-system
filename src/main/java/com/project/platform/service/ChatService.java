package com.project.platform.service;

import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * @author skm1229
 * @since 2025/8/12
 */
public interface ChatService {

    /**
     * 测试大模型交互聊天
     * @param message
     * @return
     */
    public String chat(String message);

    /**
     * 测试大模型流式交互聊天(流式响应JSON)
     * @param message
     * @return
     */
    public Flux<ChatResponse> streamResponse(String message);

    /**
     * 测试大模型流式交互聊天(流式响应字符串)
     * @param message
     * @return
     */
    public Flux<String> streamStr(String message);
}
