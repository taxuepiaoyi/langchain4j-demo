package com.bruce.langchain4jdemo.controller;

import com.bruce.langchain4jdemo.service.ChatService;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private ChatService chatService ;

    /**
     * 聊天
     * @param userMessage
     * @return
     */
    @GetMapping("/chat")
    public String chat(@RequestParam("userMessage") String userMessage) {
        return chatService.chat(userMessage);
    }

    @GetMapping("/chatByRequest")
    public String chatByRequest(@RequestParam("userMessage") String userMessage) {
        return chatService.chat(UserMessage.userMessage(userMessage));
    }

    @GetMapping("/stream")
    public SseEmitter stream(@RequestParam("userMessage") String userMessage) {
        return chatService.stream(UserMessage.userMessage(userMessage));
    }

    @GetMapping("/streamOnFlux")
    public Flux<String> streamOnFlux(@RequestParam("userMessage") String userMessage) {
        return chatService.streamOnFlux(UserMessage.userMessage(userMessage));
    }

    @PostMapping("/chatMemory")
    public Map<String, String> chatMemory(@RequestBody Map<String, String> request) {
        String memoryId = request.get("memoryId");
        if (memoryId == null || memoryId.isEmpty()) {
            throw new IllegalArgumentException("memoryId cannot be null or empty");
        }
        String userMessage = request.get("userMessage");
        String result = chatService.chatMemory(memoryId, userMessage);
        return Map.of(memoryId, result);
    }
}
