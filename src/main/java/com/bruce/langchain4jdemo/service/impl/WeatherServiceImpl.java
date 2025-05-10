package com.bruce.langchain4jdemo.service.impl;

import com.bruce.langchain4jdemo.aiservice.WeatherAiService;
import com.bruce.langchain4jdemo.service.WeatherService;
import com.bruce.langchain4jdemo.tools.WeatherTools;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    @Resource
    private QwenChatModel  qwenChatModel;

    @Resource
    private WeatherTools weatherTools;

    @Override
    public String search(String inputMsg) {
        WeatherAiService weatherAiService = getWeatherAiService();
        WeatherAiService.City cityRecord = weatherAiService.getCityName(inputMsg);
        if(cityRecord == null){
            return "抱歉，未找到该城市";
        }
        return weatherAiService.getWeather(cityRecord);
    }


    private WeatherAiService getWeatherAiService() {
        return AiServices.builder(WeatherAiService.class).chatModel(qwenChatModel).tools(weatherTools).build();
    }
}
