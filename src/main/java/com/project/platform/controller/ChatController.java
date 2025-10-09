package com.project.platform.controller;


import com.project.platform.service.ChatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author skm1229
 * @since 2025/8/12
 */
@Slf4j
@RestController
@RequestMapping("/ai")
public class ChatController {
    @Autowired
    private ChatService chatService;


    @GetMapping("/chat")
    public String chat(@RequestParam(value = "msg",defaultValue = "你是谁？") String msg) {
        log.info("ai聊天接口被调用");
        return chatService.chat(msg);
    }

    @GetMapping(value = "/chat/response",produces = "text/event-stream;charset=UTF-8")
    public Flux<ChatResponse> chatResponse(@RequestParam(value = "msg",defaultValue = "你是谁？") String msg) {
        log.info("ai聊天接口被调用");
        return chatService.streamResponse(msg);

//        Flux<String> flux = chatService.streamResponse(msg)
//                .map(chatResponse -> {
//                    return chatResponse.getResult().getOutput().getText();
//                });
//        return flux;
    }

    @GetMapping(value = "/chat/str",produces = "text/html;charset=UTF-8")
    public Flux<String> chatStr(@RequestParam(value = "msg",defaultValue = "你是谁？") String msg) {
        log.info("ai聊天接口被调用");
        return chatService.streamStr(msg);
    }
}
