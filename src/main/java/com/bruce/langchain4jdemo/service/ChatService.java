package com.bruce.langchain4jdemo.service;

import dev.langchain4j.data.message.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;


public interface ChatService {
    String chat(String userMessage);
    String chat(UserMessage userMessage) ;
    SseEmitter stream(UserMessage userMessage);

    /**
     * Flux流式输出
     * @param userMessage
     * @return
     */
    Flux<String> streamOnFlux(UserMessage userMessage) ;

}
