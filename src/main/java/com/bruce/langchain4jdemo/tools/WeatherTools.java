package com.bruce.langchain4jdemo.tools;

import dev.langchain4j.agent.tool.Tool;
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

    @Tool("获取城市的天气信息")
    public String getWeather(String city) {
        log.info("获取" + city + "的天气信息");
        return  city + "气温27℃，多云，风力2到4级";
    }
}
