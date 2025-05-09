package com.bruce.langchain4jdemo.service.impl;

import com.bruce.langchain4jdemo.component.PersistentChatMemoryStore;
import com.bruce.langchain4jdemo.dto.ImagePromptDTO;
import com.bruce.langchain4jdemo.service.Assistant;
import com.bruce.langchain4jdemo.service.ChatService;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private QwenChatModel qwenChatModel;

    @Resource
    private WanxImageModel wanxImageModel ;

    @Resource
    private QwenStreamingChatModel qwenStreamingChatModel;

    @Resource
    private PersistentChatMemoryStore persistentChatMemoryStore;

    @Override
    public String chat(String userMessage) {
        return qwenChatModel.chat(userMessage);
    }

    /**
     * 聊天
     * @param userMessage
     * @return
     */
    @Override
    public String chat(UserMessage userMessage) {
        ChatRequest chatRequest = ChatRequest.builder().messages(userMessage).build();
        return qwenChatModel.chat(chatRequest).aiMessage().text();
    }

    /**
     * 文生图
     * @param imagePromptDTO
     * @return
     */
    @Override
    public URI generateImage(ImagePromptDTO imagePromptDTO) throws Exception{
        String prompt = imagePromptDTO.getPrompt() ;
        String base64Image = imagePromptDTO.getBase64Image() ;

        // 如果有图片，则进行图片编辑
        if(base64Image != null && !base64Image.isEmpty()){
            Image image = Image.builder().base64Data(base64Image).build() ;
            return wanxImageModel.edit(image,prompt).content().url();
        }

        // 根据文本描述生成图片
        Response<Image> response = wanxImageModel.generate(prompt) ;
        return response.content().url();
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
        qwenStreamingChatModel.chat(chatRequest, new StreamingChatResponseHandler() {

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
            qwenStreamingChatModel.chat(chatRequest, new StreamingChatResponseHandler(){

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
    public String chatMemory(String memoryId, String userMessage) {
        Assistant assistant = generateAssistant() ;
        return assistant.chat(memoryId, userMessage);
    }

    private Assistant generateAssistant(){
        ChatMemoryProvider chatMemoryProvider = memoryId -> {
            MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                    .id(memoryId)
                    .maxMessages(10)
                    .chatMemoryStore(persistentChatMemoryStore)
                    .build();
            if (chatMemory.messages().isEmpty()) {
                chatMemory.add(SystemMessage.from("Welcome to the chat!"));
            }
            System.out.println("chatMemory messages:" + chatMemory.messages());
            return chatMemory;
        };
        return AiServices.builder(Assistant.class)
                .chatModel(qwenChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }
}
