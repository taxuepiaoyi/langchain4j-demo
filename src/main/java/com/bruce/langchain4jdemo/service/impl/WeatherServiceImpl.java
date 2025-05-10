package com.bruce.langchain4jdemo.service.impl;

import com.bruce.langchain4jdemo.aiservice.WeatherAiService;
import com.bruce.langchain4jdemo.service.WeatherService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    @Resource
    private WeatherAiService weatherAiService ;

    @Override
    public String search(String inputMsg) {
        WeatherAiService.City cityRecord = weatherAiService.getCityName(inputMsg);
        if(cityRecord == null){
            return "抱歉，未找到该城市";
        }
        return weatherAiService.getWeather(cityRecord);
    }
}
