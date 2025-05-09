package com.bruce.langchain4jdemo.service.impl;

import com.bruce.langchain4jdemo.component.PersistentChatMemoryStore;
import com.bruce.langchain4jdemo.service.Assistant;
import com.bruce.langchain4jdemo.service.ChatService;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;

@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private QwenChatModel chatLanguageModel;

    @Resource
    private QwenStreamingChatModel streamingChatLanguageModel;

    @Resource
    private PersistentChatMemoryStore persistentChatMemoryStore;

    @Override
    public String chat(String userMessage) {
        return chatLanguageModel.chat(userMessage);
    }

    /**
     * 聊天
     * @param userMessage
     * @return
     */
    @Override
    public String chat(UserMessage userMessage) {
        ChatRequest chatRequest = ChatRequest.builder().messages(userMessage).build();
        return chatLanguageModel.chat(chatRequest).aiMessage().text();
    }

    /**
     * 流式返回结果
     * @param userMessage
     * @return
     */
    @Override
    public SseEmitter stream(UserMessage userMessage) {
        SseEmitter emitter = new SseEmitter(0L); // 不限制超时时间
        ChatRequest chatRequest = ChatRequest.builder().messages(userMessage).build();
        streamingChatLanguageModel.chat(chatRequest, new StreamingChatResponseHandler() {

            @Override
            public void onPartialResponse(String token) {
                try {
                    emitter.send(SseEmitter.event().data(token));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                emitter.complete();
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        return emitter;
    }

    /***
     * Flux流式返回结果
     * @param userMessage
     * @return
     */
    @Override
    public Flux<String> streamOnFlux(UserMessage userMessage) {
        ChatRequest chatRequest = ChatRequest.builder().messages(userMessage).build();
        Flux<String> flux = Flux.create(emitter -> {
            streamingChatLanguageModel.chat(chatRequest, new StreamingChatResponseHandler(){

                @Override
                public void onPartialResponse(String s) {
                    emitter.next(s);
                }

                @Override
                public void onCompleteResponse(ChatResponse chatResponse) {
                   emitter.complete();
                }

                @Override
                public void onError(Throwable throwable) {
                  emitter.error(throwable);
                }
            });
        },FluxSink.OverflowStrategy.BUFFER);

        return flux;
    }

    @Override
    public String chatMemory(Integer memoryId, String userMessage) {
        ChatMemoryProvider chatMemoryProvider = storeId -> MessageWindowChatMemory.builder()
                .id(storeId)
                .maxMessages(10)
                .chatMemoryStore(persistentChatMemoryStore)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(chatLanguageModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
        return assistant.chat(memoryId, userMessage);
    }
}
