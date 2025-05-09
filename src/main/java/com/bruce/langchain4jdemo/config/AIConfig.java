package com.bruce.langchain4jdemo.config;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.message.SystemMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Value("${langchain4j.community.dashscope.wax-image-model.api-key}")
    private String imageModelApiKey;

    @Value("${langchain4j.community.dashscope.wax-image-model.model-name}")
    private String imageModelName;

    @Bean
    public WanxImageModel getnewWanxImageModel(){
        WanxImageModel wanxImageModel = WanxImageModel.builder()
                .apiKey(imageModelApiKey)
                .modelName(imageModelName)
                .build();
        return wanxImageModel;
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
