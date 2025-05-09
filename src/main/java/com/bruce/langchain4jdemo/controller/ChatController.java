package com.bruce.langchain4jdemo.controller;

import com.bruce.langchain4jdemo.dto.ImagePromptDTO;
import com.bruce.langchain4jdemo.service.ChatService;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.net.URI;
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

    /**
     * 生成图片
     * @return
     */
    @PostMapping(value = "/generateImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public URI generateImage(@RequestPart("prompt") String prompt, @RequestPart("image") MultipartFile image) throws Exception{
        ImagePromptDTO imagePromptDTO = ImagePromptDTO.builder().prompt(prompt).image(image).build() ;
        return chatService.generateImage(imagePromptDTO);
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
