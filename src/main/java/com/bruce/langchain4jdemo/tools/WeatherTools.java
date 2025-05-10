package com.bruce.langchain4jdemo.tools;

import com.bruce.langchain4jdemo.dto.WeatherResponse;
import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取天气信息
 * @author bruce.sg
 * @date 2023/10/17
 */
@Slf4j
@Component
public class WeatherTools {

    @Resource
    private WebClient webClient;
    @Value("${openweather.api-key}")
    private String openWeatherApiKey;

    // 简单的城市名映射表
    private static final Map<String, String> CITY_MAPPING = new HashMap<>();
    static {
        CITY_MAPPING.put("北京", "Beijing");
        CITY_MAPPING.put("上海", "Shanghai");
        CITY_MAPPING.put("广州", "Guangzhou");
        CITY_MAPPING.put("深圳", "Shenzhen");
        CITY_MAPPING.put("杭州", "Hangzhou");
        CITY_MAPPING.put("成都", "Chengdu");
        CITY_MAPPING.put("南京", "Nanjing");
        CITY_MAPPING.put("武汉", "Wuhan");
        CITY_MAPPING.put("西安", "Xi'an");
        CITY_MAPPING.put("重庆", "Chongqing");
        // 可以继续添加更多城市映射
    }

    @Tool("获取城市的天气信息")
    public String getWeather(String city) {
        log.info("获取" + city + "的天气信息");
        // 转换中文城市名为英文
        String englishCity = translateCityName(city);
        if (englishCity == null) {
            return "不支持查询'" + city + "'的天气，请尝试其他城市";
        }

        try {
            WeatherResponse response = fetchWeatherFromApi(englishCity).block();
            if (response == null) {
                return "无法获取" + city + "的天气数据";
            }
            return formatWeatherResponse(city, response);
        } catch (Exception e) {
            return "获取" + city + "天气时发生错误: " + e.getMessage();
        }
    }

    private String translateCityName(String city) {
        // 首先检查是否直接输入了英文
        if (city.matches("[a-zA-Z]+")) {
            return city;
        }
        // 从映射表中查找中文城市名对应的英文
        return CITY_MAPPING.get(city);
    }

    private Mono<WeatherResponse> fetchWeatherFromApi(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", city)
                        .queryParam("appid", openWeatherApiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponse.class);
    }

    private String formatWeatherResponse(String city, WeatherResponse response) {
        WeatherResponse.Main main = response.getMain();
        WeatherResponse.Weather[] weather = response.getWeather();
        String weatherDescription = weather.length > 0 ? weather[0].getDescription() : "未知";
        return String.format(
                "当前%s天气%s，温度%.1f摄氏度，湿度%d%%，气压%d hPa",
                city,
                weatherDescription,
                main.getTemp(),
                main.getHumidity(),
                main.getPressure()
        );
    }
}
