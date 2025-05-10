package com.bruce.langchain4jdemo.config;

import com.bruce.langchain4jdemo.aiservice.WeatherAiService;
import com.bruce.langchain4jdemo.tools.WeatherTools;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherToolsConfig {

    @Bean
    public WeatherAiService getWeatherAiService(QwenChatModel qwenChatModel, WeatherTools weatherTools) {
        return AiServices.builder(WeatherAiService.class).chatModel(qwenChatModel).tools(weatherTools).build();
    }
}
