package com.example.day19weather.day19WeatherPractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.day19weather.day19WeatherPractice.model.Weather;
import com.example.day19weather.day19WeatherPractice.service.WeatherService;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{country}")
    @ResponseBody
    public Weather getWeather(@PathVariable("country") String country,
            @RequestParam(name = "airQuality", required = false) String airQuality) {
        Weather weather = weatherService.getWeather(country, airQuality);

        return weather;
    }

}
