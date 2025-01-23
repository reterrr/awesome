package com.example.weather.Helpers;

import com.example.generated.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class WeatherApiHelper{

    private final RedisTemplate<String, String> redisTemplate;
    private final OkHttpClient httpClient = new OkHttpClient();
    private final String weather_base_url;
    private final String hourly_base_url;
    private final String daily_base_url;
    private final String api_key;
    private final WeatherParse weatherParse = new WeatherParse();

    public WeatherApiHelper(RedisTemplate<String, String> redisTemplate,
                            @Value("${WEATHER_URL}") String weather_base_url,
                            @Value("${HOURLY_URL}") String hourly_base_url,
                            @Value("${DAILY_URL}") String daily_base_url,
                            @Value("${WEATHER_API}") String api_key) {
        this.redisTemplate = redisTemplate;
        this.weather_base_url = weather_base_url;
        this.hourly_base_url = hourly_base_url;
        this.daily_base_url = daily_base_url;
        this.api_key = api_key;
    }

    public void fetchWeatherFromAPI(String city, StreamObserver<CurrentWeatherResponse> responseObserver, String cacheKey) {
        String url = String.format(weather_base_url, city, api_key);
        Request request = new Request.Builder().url(url).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                responseObserver.onError(new StatusRuntimeException(
                        Status.INTERNAL.withDescription("Failed to fetch weather data: " + response.message())));
                return;
            }

            JSONObject jsonObject = new JSONObject(response.body().string());
            CurrentWeatherResponse.Builder responseBuilder = weatherParse.parseWeatherResponse(jsonObject);

            redisTemplate.opsForValue().set(cacheKey, jsonObject.toString(), 90, TimeUnit.MINUTES);

            responseObserver.onNext(responseBuilder.build());
        } catch (IOException e) {
            responseObserver.onError(e);
        }
    }

    public String fetchWeather(String city, String urls) {
        String url = String.format(urls, city, api_key);
        Request request = new Request.Builder().url(url).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch weather data: " + response.message());
            }

            JSONObject jsonObject = new JSONObject(response.body().string());

            String cacheKey = "weather:" + city + ":persistent";
            String newWeatherDataStr = jsonObject.toString();
            String cachedWeatherData = redisTemplate.opsForValue().get(cacheKey);

            System.out.println("Old Cache: " + cachedWeatherData);
            System.out.println("New Data: " + newWeatherDataStr);

            return jsonObject.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void fetchHourlyFromAPI(String city, StreamObserver<HourlyWeatherResponse> responseObserver, String cacheKey) {
        String url = String.format(hourly_base_url, city, api_key);
        Request request = new Request.Builder().url(url).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                responseObserver.onError(new StatusRuntimeException(
                        Status.INTERNAL.withDescription("Failed to fetch hourly weather data: " + response.message())));
                return;
            }

            JSONObject jsonObject = new JSONObject(response.body().string());
            HourlyWeatherResponse hourlyWeatherResponse = weatherParse.parseHourlyResponse(jsonObject);

            redisTemplate.opsForValue().set(cacheKey, jsonObject.toString(), 90, TimeUnit.MINUTES);

            responseObserver.onNext(hourlyWeatherResponse);
        } catch (IOException e) {
            responseObserver.onError(e);
        }
    }

    public void fetchDailyFromAPI(String city, StreamObserver<ForecastResponse> responseObserver, String cacheKey) {
        String url = String.format(daily_base_url, city, api_key);
        Request request = new Request.Builder().url(url).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                responseObserver.onError(new StatusRuntimeException(
                        Status.INTERNAL.withDescription("Failed to fetch daily weather data: " + response.message())));
                return;
            }

            JSONObject jsonObject = new JSONObject(response.body().string());
            ForecastResponse forecastResponse = weatherParse.parseForecastResponse(jsonObject);

            redisTemplate.opsForValue().set(cacheKey, jsonObject.toString(), 5, TimeUnit.MINUTES);

            responseObserver.onNext(forecastResponse);
        } catch (IOException e) {
            responseObserver.onError(e);
        }
    }



}

