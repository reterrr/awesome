package com.example.weather.controllers;

import com.example.generated.*;
import com.example.weather.Helpers.WeatherApiHelper;
import com.example.weather.Helpers.WeatherKeys;
import com.example.weather.Helpers.WeatherParse;
import io.grpc.stub.StreamObserver;
import okhttp3.OkHttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class WeatherService extends WeatherServiceGrpc.WeatherServiceImplBase {


    @Value("${WEATHER_URL}")
    private String weather_base_url;

    @Value("${HOURLY_URL}")
    private String hourly_base_url;

    @Value("${DAILY_URL}")
    private String daily_base_url;

    @Value("${WEATHER_API}")
    private String api_key;

    private final RedisTemplate<String, String> redisTemplate;
    private final WeatherApiHelper weatherApiHelper;
    private final WeatherParse weatherParse;
    private final OkHttpClient httpClient;

    public WeatherService(RedisTemplate<String, String> redisTemplate,
                          WeatherApiHelper weatherApiHelper,
                          WeatherParse weatherParse,
                          @Value("${WEATHER_URL}") String weatherBaseUrl,
                          @Value("${HOURLY_URL}") String hourlyBaseUrl,
                          @Value("${DAILY_URL}") String dailyBaseUrl,
                          @Value("${WEATHER_API}") String apiKey) {
        this.redisTemplate = redisTemplate;
        this.weatherApiHelper = weatherApiHelper;
        this.weatherParse = weatherParse;
        this.weather_base_url = weatherBaseUrl;
        this.hourly_base_url = hourlyBaseUrl;
        this.daily_base_url = dailyBaseUrl;
        this.api_key = apiKey;
        this.httpClient = new OkHttpClient();
    }

    @Override
    public StreamObserver<CurrentWeatherRequest> getCurrentWeather(StreamObserver<CurrentWeatherResponse> responseObserver) {
        return new StreamObserver<CurrentWeatherRequest>() {

            @Override
            public void onNext(CurrentWeatherRequest currentWeatherRequest) {
                String city = currentWeatherRequest.getCity();
                String cacheKey = WeatherKeys.WEATHER.getLabel() + city;

                String persistentCacheKey = "weather:" + city + ":persistent";
                String cachedWeatherData = redisTemplate.opsForValue().get(persistentCacheKey);

                if (StringUtils.hasText(cachedWeatherData)) {
                    System.out.println("Fetched persistent weather data for " + city);
                    responseObserver.onNext(weatherParse.parseWeatherResponse(new JSONObject(cachedWeatherData)).build());
                } else {

                    String notPersistentCacheKey = "weather:" + city + ":not_persistent";
                    cachedWeatherData = redisTemplate.opsForValue().get(notPersistentCacheKey);

                    if (StringUtils.hasText(cachedWeatherData)) {
                        System.out.println("Fetched non-persistent weather data for " + city);
                        responseObserver.onNext(weatherParse.parseWeatherResponse(new JSONObject(cachedWeatherData)).build());
                    } else {
                        System.out.println("No cached data for " + city + ". Fetching from API.");
                        weatherApiHelper.fetchWeatherFromAPI(city, responseObserver, cacheKey);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }


        };
    }

    @Scheduled(fixedRate = 50000)
    public void fetchWeatherForPersistentCitiesCurrent() {
        FetchAndUpdate("weather:*",weather_base_url);
    }

    @Override
    public StreamObserver<HourlyWeatherRequest> getHourlyWeather(StreamObserver<HourlyWeatherResponse> responseObserver) {
        return new StreamObserver<HourlyWeatherRequest>() {
            @Override
            public void onNext(HourlyWeatherRequest hourlyWeatherRequest) {
                String city = hourlyWeatherRequest.getCity();
                String cacheKey = WeatherKeys.HOURLY.getLabel() + city;

                String persistentCacheKey = "hourly:" + city + ":persistent";
                String cachedWeatherData = redisTemplate.opsForValue().get(persistentCacheKey);

                if (StringUtils.hasText(cachedWeatherData)) {
                    System.out.println("Fetched persistent hourly weather data for " + city);
                    responseObserver.onNext(weatherParse.parseHourlyResponse(new JSONObject(cachedWeatherData)));
                } else {
                    String notPersistentCacheKey = "hourly:" + city + ":not_persistent";
                    cachedWeatherData = redisTemplate.opsForValue().get(notPersistentCacheKey);

                    if (StringUtils.hasText(cachedWeatherData)) {
                        System.out.println("Fetched non-persistent hourly weather data for " + city);
                        responseObserver.onNext(weatherParse.parseHourlyResponse(new JSONObject(cachedWeatherData)));
                    } else {
                        System.out.println("No cached data for " + city + ". Fetching from API.");
                        weatherApiHelper.fetchHourlyFromAPI(city, responseObserver, cacheKey);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }



        };
    }

    @Scheduled(fixedRate = 50000)
    public void fetchWeatherForPersistentCitiesHourly() {
        FetchAndUpdate("hourly:*",hourly_base_url);
    }

    @Override
    public StreamObserver<ForecastRequest> getForecastWeather(StreamObserver<ForecastResponse> responseObserver) {
        return new StreamObserver<ForecastRequest>() {
            @Override
            public void onNext(ForecastRequest forecastRequest) {
                String city = forecastRequest.getCity();
                String cacheKey = WeatherKeys.DAILY.getLabel() + city;

                String persistentCacheKey = "daily:" + city + ":persistent";
                String cachedWeatherData = redisTemplate.opsForValue().get(persistentCacheKey);

                if (StringUtils.hasText(cachedWeatherData)) {
                    System.out.println("Fetched persistent daily forecast data for " + city);
                    responseObserver.onNext(weatherParse.parseForecastResponse(new JSONObject(cachedWeatherData)));
                } else {
                    String notPersistentCacheKey = "daily:" + city + ":not_persistent";
                    cachedWeatherData = redisTemplate.opsForValue().get(notPersistentCacheKey);

                    if (StringUtils.hasText(cachedWeatherData)) {
                        System.out.println("Fetched non-persistent daily forecast data for " + city);
                        responseObserver.onNext(weatherParse.parseForecastResponse(new JSONObject(cachedWeatherData)));
                    } else {
                        System.out.println("No cached data for " + city + ". Fetching from API.");
                        weatherApiHelper.fetchDailyFromAPI(city, responseObserver, cacheKey);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }

        };
    }

    @Scheduled(fixedRate = 50000)
    public void fetchWeatherForPersistentCitiesDaily() {
        FetchAndUpdate("daily:*",daily_base_url);
    }

    @Override
    public void updateLocations(UpdateLocationsRequest request,StreamObserver<com.google.protobuf.Empty> responseObserver){

        List<String> CityNames = request.getNamesList() ;

        Set<String> keys = redisTemplate.keys("*:*");

        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                String[] parts = key.split(":");
                if (parts.length < 2) {
                    continue;
                }
                String type = parts[0];
                String cityName = parts[1];


                String currentStatus = null;
                if (key.endsWith(":persistent")) {
                    currentStatus = "persistent";
                } else if (key.endsWith(":not_persistent")) {
                    currentStatus = "not_persistent";
                } else {
                    currentStatus = null;
                }

                String desiredStatus = CityNames.contains(cityName) ? "persistent" : "not_persistent";
                if (currentStatus == null || !currentStatus.equals(desiredStatus)) {
                    String newKey = type + ":" + cityName + ":" + desiredStatus;

                    redisTemplate.rename(key, newKey);

                    if (desiredStatus.equals("persistent")) {
                        redisTemplate.persist(newKey);
                    } else {
                        redisTemplate.expire(newKey, 10, TimeUnit.MINUTES);
                    }
                }
            }
        responseObserver.onNext(com.google.protobuf.Empty.getDefaultInstance());
        responseObserver.onCompleted();
        }
    }

    private void FetchAndUpdate(String redisKey, String url) {

        Set<String> keys = redisTemplate.keys(redisKey);

        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                String[] parts = key.split(":");
                if (parts.length < 2) {
                    continue;
                }
                String type = parts[0];
                String cityName = parts[1];
                System.out.println("Weather data for " + cityName + " updated in Redis.");

                String suffix = "";
                if (key.endsWith(":persistent")) {
                    suffix = ":persistent";
                } else if (key.endsWith(":not_persistent")) {
                    suffix = ":not_persistent";
                }

                String cacheKey = type + ":" + cityName + suffix;

                String newWeatherData = weatherApiHelper.fetchWeather(cityName, url);

                if (newWeatherData != null) {
                    String cachedWeatherData = redisTemplate.opsForValue().get(cacheKey);

                    if (cachedWeatherData == null || !cachedWeatherData.equals(newWeatherData)) {
                        System.out.println("Updating weather data in Redis for " + cacheKey);

                        redisTemplate.opsForValue().set(cacheKey, newWeatherData);

                    } else {
                        System.out.println("Weather data for " + cityName + " has not changed, skipping Redis update.");
                    }
                } else {
                    System.out.println("Failed to fetch weather data for " + cityName);
                }
            }
        }
    }
}






