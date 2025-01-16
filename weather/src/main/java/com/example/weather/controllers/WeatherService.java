package com.example.weather.controllers;

import com.example.generated.*;
import com.example.weather.Helpers.WeatherKeys;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
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

    private final OkHttpClient httpClient = new OkHttpClient();

    public WeatherService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public StreamObserver<CurrentWeatherRequest> getCurrentWeather(StreamObserver<CurrentWeatherResponse> responseObserver) {
        return new StreamObserver<CurrentWeatherRequest>() {

            @Override
            public void onNext(CurrentWeatherRequest currentWeatherRequest) {
                String city = currentWeatherRequest.getCity();
                String cacheKey = WeatherKeys.WEATHER.getLabel() + city.toLowerCase();

                String cachedWeatherData = redisTemplate.opsForValue().get(cacheKey);

                if (StringUtils.hasText(cachedWeatherData)) {
                    responseObserver.onNext(parseWeatherResponse(new JSONObject(cachedWeatherData)).build());
                } else {
                    fetchWeatherFromAPI(city, responseObserver, cacheKey);
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

            private void fetchWeatherFromAPI(String city, StreamObserver<CurrentWeatherResponse> responseObserver, String cacheKey) {
                String url = String.format(weather_base_url, city, api_key);
                Request request = new Request.Builder().url(url).build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        responseObserver.onError(new StatusRuntimeException(
                                Status.INTERNAL.withDescription("Failed to fetch weather data: " + response.message())));
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    CurrentWeatherResponse.Builder responseBuilder = parseWeatherResponse(jsonObject);

                    redisTemplate.opsForValue().set(cacheKey, jsonObject.toString(), 5, TimeUnit.MINUTES);

                    responseObserver.onNext(responseBuilder.build());
                } catch (IOException e) {
                    responseObserver.onError(e);
                }
            }

            private CurrentWeatherResponse.Builder parseWeatherResponse(JSONObject jsonResponse) {
                CurrentWeatherResponse.Builder responseBuilder = CurrentWeatherResponse.newBuilder();
                responseBuilder.setCityName(jsonResponse.getString("name"))
                        .setCountry(jsonResponse.getJSONObject("sys").getString("country"))
                        .setCode(200);

                JSONObject weatherData = jsonResponse.getJSONArray("weather").getJSONObject(0);
                responseBuilder.getCurrentWeatherBuilder()
                        .setTemp(jsonResponse.getJSONObject("main").getFloat("temp"))
                        .setDescription(weatherData.getString("description"))
                        .setIcon(weatherData.getString("icon"))
                        .setFeelsLike(jsonResponse.getJSONObject("main").getFloat("feels_like"))
                        .setTempMin(jsonResponse.getJSONObject("main").getFloat("temp_min"))
                        .setTempMax(jsonResponse.getJSONObject("main").getFloat("temp_max"))
                        .setMain(weatherData.getString("main"));

                JSONObject windData = jsonResponse.getJSONObject("wind");
                responseBuilder.setWind(parseWind(windData));

                responseBuilder.setHumidity(jsonResponse.getJSONObject("main").getInt("humidity"))
                        .setVisibility(jsonResponse.optInt("visibility", 0))
                        .setSunrise(convertUnixToMinutes(jsonResponse.getJSONObject("sys").getInt("sunrise")))
                        .setSunset(convertUnixToMinutes(jsonResponse.getJSONObject("sys").getInt("sunset")))
                        .setDt(convertUnixToMinutes(jsonResponse.getInt("dt")));

                return responseBuilder;
            }
        };

    }

    @Override
    public StreamObserver<HourlyWeatherRequest> getHourlyWeather(StreamObserver<HourlyWeatherResponse> responseObserver) {
        return new StreamObserver<HourlyWeatherRequest>() {
            @Override
            public void onNext(HourlyWeatherRequest hourlyWeatherRequest) {
                String city = hourlyWeatherRequest.getCity();
                String cacheKey = WeatherKeys.HOURLY.getLabel() + city.toLowerCase();

                String cachedWeatherData = redisTemplate.opsForValue().get(cacheKey);

                if (StringUtils.hasText(cachedWeatherData)) {
                    responseObserver.onNext(parseHourlyResponse(new JSONObject(cachedWeatherData)));
                } else {
                    fetchHourlyFromAPI(city, responseObserver, cacheKey);
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

            private void fetchHourlyFromAPI(String city, StreamObserver<HourlyWeatherResponse> responseObserver, String cacheKey) {
                String url = String.format(hourly_base_url, city, api_key);
                Request request = new Request.Builder().url(url).build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        responseObserver.onError(new StatusRuntimeException(
                                Status.INTERNAL.withDescription("Failed to fetch hourly weather data: " + response.message())));
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    HourlyWeatherResponse hourlyWeatherResponse = parseHourlyResponse(jsonObject);

                    redisTemplate.opsForValue().set(cacheKey, jsonObject.toString(), 5, TimeUnit.MINUTES);

                    responseObserver.onNext(hourlyWeatherResponse);
                } catch (IOException e) {
                    responseObserver.onError(e);
                }
            }

            private HourlyWeatherResponse parseHourlyResponse(JSONObject jsonResponse) {
                HourlyWeatherResponse.Builder responseBuilder = HourlyWeatherResponse.newBuilder();

                String cityName = jsonResponse.getJSONObject("city").getString("name");
                String countryName = jsonResponse.getJSONObject("city").getString("country");
                responseBuilder.setCityName(cityName);
                responseBuilder.setCountry(countryName);

                JSONArray hourlyWeatherArray = jsonResponse.getJSONArray("list");

                Map<String, DailyWeather.Builder> dailyWeatherMap = new HashMap<>();

                for (int i = 0; i < hourlyWeatherArray.length(); i++) {
                    JSONObject hourlyWeatherData = hourlyWeatherArray.getJSONObject(i);

                    String date = convertUnixToDate(hourlyWeatherData.getLong("dt"));
                    String formattedDate = date.substring(0, 10);

                    DailyWeather.Builder dailyWeatherBuilder = dailyWeatherMap.get(formattedDate);
                    if (dailyWeatherBuilder == null) {
                        dailyWeatherBuilder = DailyWeather.newBuilder()
                                .setDate(formattedDate);
                        dailyWeatherMap.put(formattedDate, dailyWeatherBuilder);
                    }

                    HourlyWeather.Builder hourlyWeatherBuilder = HourlyWeather.newBuilder()
                            .setDt(convertUnixToMinutes(hourlyWeatherData.getLong("dt")))
                            .setMain(parseMain(hourlyWeatherData.getJSONObject("main")))
                            .setWeather(parseWeatherDescription(hourlyWeatherData.getJSONArray("weather").getJSONObject(0)))
                            .setWind(parseWind(hourlyWeatherData.getJSONObject("wind")))
                            .setPop(hourlyWeatherData.optFloat("pop", 0.0f))
                            .setClouds(parseClouds(hourlyWeatherData.getJSONObject("clouds")))
                            .setSys(parseSys(hourlyWeatherData.getJSONObject("sys")));

                    if (hourlyWeatherData.has("rain")) {
                        hourlyWeatherBuilder.setRain(parseRain(hourlyWeatherData.getJSONObject("rain")));
                    }
                    if (hourlyWeatherData.has("snow")) {
                        hourlyWeatherBuilder.setSnow(parseSnow(hourlyWeatherData.getJSONObject("snow")));
                    }

                    dailyWeatherBuilder.addHourlyWeather(hourlyWeatherBuilder);

                }

                List<String> sortedDates = new ArrayList<>(dailyWeatherMap.keySet());
                Collections.sort(sortedDates);

                for (String date : sortedDates) {
                    DailyWeather.Builder dailyWeatherBuilder = dailyWeatherMap.get(date);
                    responseBuilder.addDailyWeather(dailyWeatherBuilder.build());
                }

                return responseBuilder.build();
            }

            private Main parseMain(JSONObject mainData) {
                return Main.newBuilder()
                        .setTemp(mainData.getFloat("temp"))
                        .setFeelsLike(mainData.getFloat("feels_like"))
                        .setTempMin(mainData.getFloat("temp_min"))
                        .setTempMax(mainData.getFloat("temp_max"))
                        .setPressure(mainData.getInt("pressure"))
                        .setSeaLevel(mainData.getInt("sea_level"))
                        .setGrndLevel(mainData.getInt("grnd_level"))
                        .setHumidity(mainData.getInt("humidity"))
                        .setTempKf(mainData.getFloat("temp_kf"))
                        .build();
            }

            private Rain parseRain(JSONObject rainData) {
                if (rainData != null) {
                    return Rain.newBuilder().setRain(rainData.getFloat("1h")).build();
                }
                return Rain.newBuilder().setRain(0).build();
            }

            private Snow parseSnow(JSONObject snowData) {
                return Snow.newBuilder()
                        .setSnow(snowData.getFloat("1h"))
                        .build();
            }

            private Clouds parseClouds(JSONObject cloudsData) {
                return Clouds.newBuilder().setAll(cloudsData.getInt("all")).build();
            }

            private Sys parseSys(JSONObject sysData) {
                return Sys.newBuilder().setPod(sysData.getString("pod")).build();
            }

        };

    }

    public StreamObserver<ForecastRequest> getForecastWeather(StreamObserver<ForecastResponse> responseObserver) {
        return new StreamObserver<ForecastRequest>() {
            @Override
            public void onNext(ForecastRequest forecastRequest) {
                String city = forecastRequest.getCity();
                String cacheKey = WeatherKeys.DAILY.getLabel() + city.toLowerCase();

                String cachedWeatherData = redisTemplate.opsForValue().get(cacheKey);

                if (StringUtils.hasText(cachedWeatherData)) {
                    responseObserver.onNext(parseForecastResponse(new JSONObject(cachedWeatherData)));

                } else {
                    fetchDailyFromAPI(city, responseObserver, cacheKey);
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

            private void fetchDailyFromAPI(String city, StreamObserver<ForecastResponse> responseObserver, String cacheKey) {
                String url = String.format(daily_base_url, city, api_key);
                Request request = new Request.Builder().url(url).build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        responseObserver.onError(new StatusRuntimeException(
                                Status.INTERNAL.withDescription("Failed to fetch hourly weather data: " + response.message())));
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(response.body().string());
                   ForecastResponse forecastResponse = parseForecastResponse(jsonObject);

                    redisTemplate.opsForValue().set(cacheKey, jsonObject.toString(), 5, TimeUnit.MINUTES);

                    responseObserver.onNext(forecastResponse);
                } catch (IOException e) {
                    responseObserver.onError(e);
                }
            }

            private ForecastResponse parseForecastResponse(JSONObject jsonResponse) {
                ForecastResponse.Builder forecastResponseBuilder = ForecastResponse.newBuilder();

                String cityName = jsonResponse.getJSONObject("city").getString("name");
                String countryName = jsonResponse.getJSONObject("city").getString("country");
                forecastResponseBuilder.setCity(cityName);
                forecastResponseBuilder.setCountry(countryName);

                JSONArray forecastDayArray = jsonResponse.optJSONArray("list");
                if (forecastDayArray == null) {
                    return forecastResponseBuilder.build();
                }

                Map<String, ForecastDay.Builder> forecastDayMap = new HashMap<>();

                for (int i = 0; i < forecastDayArray.length(); i++) {
                    JSONObject forecastDayData = forecastDayArray.optJSONObject(i);

                    long dt = forecastDayData.optLong("dt", 0);
                    String date = convertUnixToDate(dt);
                    String formattedDate = date.substring(0, 10);

                    ForecastDay.Builder forecastDayBuilder = forecastDayMap.get(formattedDate);
                    if (forecastDayBuilder == null) {
                        forecastDayBuilder = ForecastDay.newBuilder()
                                .setDt(formattedDate);
                        forecastDayMap.put(formattedDate, forecastDayBuilder);
                    }

                    forecastDayBuilder.setSunrise(convertUnixToMinutes(forecastDayData.optLong("sunrise", 0)));
                    forecastDayBuilder.setSunset(convertUnixToMinutes(forecastDayData.optLong("sunset", 0)));
                    forecastDayBuilder.setTemp(parseTemp(forecastDayData.optJSONObject("temp")));
                    forecastDayBuilder.setFeelsLike(parseFeelsLike(forecastDayData.optJSONObject("feels_like")));
                    forecastDayBuilder.setPressure(forecastDayData.optInt("pressure", 0));
                    forecastDayBuilder.setHumidity(forecastDayData.optInt("humidity", 0));
                    forecastDayBuilder.setClouds(forecastDayData.getInt("clouds"));
                    forecastDayBuilder.setRain(forecastDayData.optFloat("rain", 0));
                    forecastDayBuilder.setSnow(forecastDayData.optFloat("snow", 0));


                    JSONArray weatherArray = forecastDayData.optJSONArray("weather");
                    if (weatherArray != null) {
                        for (int j = 0; j < weatherArray.length(); j++) {
                            JSONObject weatherDescription = weatherArray.optJSONObject(j);
                            if (weatherDescription != null) {
                                forecastDayBuilder.addWeather(parseWeatherDescription(weatherDescription));
                            }
                        }
                    }

                }

                List<String> sortedDates = new ArrayList<>(forecastDayMap.keySet());
                Collections.sort(sortedDates);

                for (String date : sortedDates) {
                    ForecastDay.Builder forecastDayBuilder = forecastDayMap.get(date);
                    forecastResponseBuilder.addList(forecastDayBuilder.build());
                }

                return forecastResponseBuilder.build();
            }


            private Temp parseTemp(JSONObject tempData) {
                return Temp.newBuilder()
                        .setDay(tempData.optFloat("day", 0.0f))
                        .setMin(tempData.optFloat("min", 0.0f))
                        .setMax(tempData.optFloat("max", 0.0f))
                        .setNight(tempData.optFloat("night", 0.0f))
                        .setEve(tempData.optFloat("eve", 0.0f))
                        .setMorn(tempData.optFloat("morn", 0.0f))
                        .build();
            }

            private FeelsLike parseFeelsLike(JSONObject feelsLikeData) {
                return FeelsLike.newBuilder()
                        .setDay(feelsLikeData.optFloat("day", 0.0f))
                        .setNight(feelsLikeData.optFloat("night", 0.0f))
                        .setEve(feelsLikeData.optFloat("eve", 0.0f))
                        .setMorn(feelsLikeData.optFloat("morn", 0.0f))
                        .build();
            }
        };

    }

    private Wind parseWind(JSONObject windData) {
        return Wind.newBuilder()
                .setSpeed(windData.getFloat("speed"))
                .setDeg(windData.getInt("deg"))
                .setGust(windData.getFloat("gust"))
                .build();
    }

    private WeatherDescription parseWeatherDescription(JSONObject weatherData) {
        return WeatherDescription.newBuilder()
                .setId(weatherData.getInt("id"))
                .setMain(weatherData.getString("main"))
                .setDescription(weatherData.getString("description"))
                .setIcon(weatherData.getString("icon"))
                .build();
    }

    private String convertUnixToMinutes(long timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
        java.util.Date date = new java.util.Date(timestamp * 1000);
        return sdf.format(date);
    }

    private String convertUnixToDate(long timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date(timestamp * 1000);
        return sdf.format(date);
    }

}

