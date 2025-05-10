package com.bruce.langchain4jdemo.controller;

import com.bruce.langchain4jdemo.service.WeatherService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Resource
    private WeatherService weatherService ;

    /**
     * 获取天气信息
     * @param inputMsg
     * @return
     */
    @GetMapping("/search")
    public String search(@RequestParam("inputMsg") String inputMsg){
        return weatherService.search(inputMsg);
    }
}
