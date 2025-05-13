package com.bruce.langchain4jdemo.config;
import com.bruce.langchain4jdemo.aiservice.UserAiService;
import com.bruce.langchain4jdemo.aiservice.WeatherAiService;
import com.bruce.langchain4jdemo.tools.WeatherTools;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;

import dev.langchain4j.service.AiServices;
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

    @Bean
    public UserAiService getUserAiService(QwenChatModel qwenChatModel){
        return AiServices.create(UserAiService.class, qwenChatModel);
    }

    @Bean
    public WeatherAiService getWeatherAiService(QwenChatModel qwenChatModel, WeatherTools weatherTools) {
        return AiServices.builder(WeatherAiService.class).chatModel(qwenChatModel).tools(weatherTools).build();
    }
}
