package com.bruce.langchain4jdemo.tools;

import com.bruce.langchain4jdemo.aiservice.WeatherAiService;
import com.bruce.langchain4jdemo.dto.WeatherResponseDTO;
import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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


    @Tool("获取当前城市的天气情况")
    public String getWeather(WeatherAiService.City city) {
        if(city == null){
            return "抱歉，未找到该城市";
        }
        String cityChineseName = city.cityChineseName();
        String cityEnglishName = city.cityEnglishName();
        try {
            WeatherResponseDTO response = fetchWeatherFromApi(cityEnglishName).block();
            if (response == null) {
                return "无法获取" + cityChineseName + "的天气数据";
            }
            return formatWeatherResponse(cityChineseName, response);
        } catch (Exception e) {
            return "获取" + city + "天气时发生错误: " + e.getMessage();
        }
    }


    private Mono<WeatherResponseDTO> fetchWeatherFromApi(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", city)
                        .queryParam("appid", openWeatherApiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponseDTO.class);
    }

    private String formatWeatherResponse(String city, WeatherResponseDTO response) {
        WeatherResponseDTO.Main main = response.getMain();
        WeatherResponseDTO.Weather[] weather = response.getWeather();
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
