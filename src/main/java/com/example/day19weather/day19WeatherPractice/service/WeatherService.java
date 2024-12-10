package com.example.day19weather.day19WeatherPractice.service;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.day19weather.day19WeatherPractice.model.Weather;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.key}")
    private String apiKey;

    @Value("${weather.api.base-url}")
    private String weatherApiBaseUrl;

    public Weather getWeather(String country, String aqi) {
        try {

            // build the API url
            String url = buildUrl(apiKey, country, aqi);
            System.out.println("API URL: " + url);

            // Make the request to the API
            String response = restTemplate.getForObject(url, String.class);
            Weather weather = parseWeatherResponse(response, aqi);

            return weather;
 
        } catch (Exception e) {
            System.err.println("Error occured while fetchung weather data: " + e.getMessage());
            throw new RuntimeException("Failed to fetch weather data", e);
        }

    }

    private String buildUrl(String apiKey, String country, String aqi) {

        // build the URL
        String url = UriComponentsBuilder
                    .fromUriString(weatherApiBaseUrl)
                    .queryParam("key", apiKey)
                    .queryParam("q", country)
                    .queryParam("aqi", aqi)
                    .toUriString();
        return url;
    }

    public Weather parseWeatherResponse(String response, String aqi) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(response))) {
            JsonObject root = jsonReader.readObject();
            JsonObject location = root.getJsonObject("location");

            String name = location.getString("name");
            String region = location.getString("region");
            String country = location.getString("country");
            Double latitude = location.getJsonNumber("lat").doubleValue();
            Double longtitude = location.getJsonNumber("lon").doubleValue();

            Weather weather = new Weather(name, region, country, latitude, longtitude);

            if ("yes".equalsIgnoreCase(aqi)) {
                JsonObject airQuality = root.getJsonObject("current").getJsonObject("air_quality");
                weather.setAirQualityData(airQuality);

            }

            return weather;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing weather API response", e);
        }

     
    }

    
}
