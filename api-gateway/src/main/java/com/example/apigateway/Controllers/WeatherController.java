package com.example.apigateway.Controllers;


import com.example.apigateway.Clients.WeatherClient;
import com.example.apigateway.Helpers.Mapper;
import com.example.apigateway.Responses.ForecastWeatherResponse;
import com.example.apigateway.Responses.HourlyResponse;
import com.example.apigateway.Responses.WeatherResponse;
import com.example.generated.*;
import jakarta.validation.constraints.NotBlank;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final Mapper mapper;

    @Autowired
    public WeatherController(Mapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping(value = "/{city_name}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<WeatherResponse> streamWeatherUpdates(@PathVariable @NotBlank String city_name) {
        return Flux.interval(Duration.ofSeconds(5))
                .map(tick -> {
                    try {
                        var weatherData = WeatherClient.getCurrentWeather(city_name);

                        return mapper.mapToWeatherResponse(weatherData);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return new WeatherResponse();
                    }
                });
    }

    @GetMapping(value = "/hourly/{city_name}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<HourlyResponse> streamHourlyUpdates(@PathVariable @NotBlank String city_name) {
        return Flux.interval(Duration.ofSeconds(5)).map(tick -> {
            try {
                var weatherData = WeatherClient.getHourlyWeather(city_name);

                return mapper.mapToHourlyResponse(weatherData);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new HourlyResponse();
            }
        });
    }

    @GetMapping(value = "/forecast/{city_name}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ForecastWeatherResponse> streamForecastUpdates(@PathVariable @NotBlank String city_name) {
        return Flux.interval(Duration.ofSeconds(5)).map(tick -> {
            try {
                var weatherData = WeatherClient.getForecastWeather(city_name);
                return mapper.mapToForecastResponse(weatherData);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new ForecastWeatherResponse();
            }
        });

    }
}