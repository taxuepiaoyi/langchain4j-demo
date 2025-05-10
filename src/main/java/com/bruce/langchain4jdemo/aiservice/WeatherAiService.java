package com.bruce.langchain4jdemo.aiservice;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface WeatherAiService {

    record City(String cityChineseName, String cityEnglishName){}

    @UserMessage("从{{inputMsg}}中提取城市名称，如果这个城市名称是中文，请转换成对应的英文名称")
    City getCityName(@V("inputMsg") String inputMsg);

    String getWeather(City city);
}
