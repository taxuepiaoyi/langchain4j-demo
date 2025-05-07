package com.bruce.langchain4jdemo.config;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Value("${langchain4j.community.dashscope.api-key}")
    private String tongyiAiKey ;

    @Value("${langchain4j.community.dashscope.model-name}")
    private String modelName ;

    @Bean
    public ChatLanguageModel getChatLanguageModel(){
        return QwenChatModel.builder().apiKey(tongyiAiKey).modelName(modelName).build() ;
    }

    /**
     * 获取流式输出模型
     * @return
     */
    @Bean
    public StreamingChatLanguageModel getStreamingChatLanguageModel(){
        return QwenStreamingChatModel.builder().apiKey(tongyiAiKey).modelName(modelName).build() ;
    }

    /**
     * 设置AI系统角色
     * @return
     */
    @Bean
    public SystemMessage getSystemMessage(){
        return SystemMessage.from("你是一个智能聊天助手");
    }
}
