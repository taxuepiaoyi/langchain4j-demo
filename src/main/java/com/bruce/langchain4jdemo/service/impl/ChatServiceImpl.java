package com.bruce.langchain4jdemo.service.impl;

import com.bruce.langchain4jdemo.service.ChatService;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import static dev.langchain4j.model.LambdaStreamingResponseHandler.onNextAndError;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;

@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private ChatLanguageModel chatLanguageModel;

    @Resource
    private StreamingChatLanguageModel streamingChatLanguageModel;

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
}
