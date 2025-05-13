package com.bruce.langchain4jdemo.tools;

import com.bruce.langchain4jdemo.aiservice.WeatherAiService;
import com.bruce.weather.service.WeatherService;
import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 获取天气信息
 * @author bruce.sg
 * @date 2023/10/17
 */
@Slf4j
@Component
public class WeatherTools {

    @Resource
    private WeatherService weatherService;


    @Tool("获取当前城市的天气情况")
    public String getWeather(WeatherAiService.City city) {
        if(city == null){
            return "抱歉，未找到该城市";
        }
        String cityEnglishName = city.cityEnglishName();
        String cityChineseName = city.cityChineseName();
        try {
            return weatherService.getWeather(cityEnglishName);
        } catch (Exception e) {
            return "获取" + cityChineseName + "天气时发生错误: " + e.getMessage();        }
    }
}
