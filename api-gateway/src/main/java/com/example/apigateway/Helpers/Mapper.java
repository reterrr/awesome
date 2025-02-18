package com.example.apigateway.Helpers;

import com.example.apigateway.Responses.ForecastWeatherResponse;
import com.example.apigateway.Responses.HourlyResponse;
import com.example.apigateway.Responses.WeatherResponse;
import com.example.generated.CurrentWeatherResponse;
import com.example.generated.ForecastResponse;
import com.example.generated.HourlyWeatherResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mapper {


    public WeatherResponse mapToWeatherResponse(CurrentWeatherResponse weatherData) {
        WeatherResponse response = new WeatherResponse();

        WeatherResponse.CurrentWeather currentWeather = new WeatherResponse.CurrentWeather();
        currentWeather.setDescription(weatherData.getCurrentWeather().getDescription());
        currentWeather.setIcon(weatherData.getCurrentWeather().getIcon());
        currentWeather.setFeels_like(weatherData.getCurrentWeather().getFeelsLike());
        currentWeather.setTemperature_max(weatherData.getCurrentWeather().getTempMax());
        currentWeather.setTemperature_min(weatherData.getCurrentWeather().getTempMin());
        currentWeather.setTemperature(weatherData.getCurrentWeather().getTemp());
        currentWeather.setMain(weatherData.getCurrentWeather().getMain());

        response.setCurrentWeather(currentWeather);

        WeatherResponse.Wind wind = new WeatherResponse.Wind();
        wind.setSpeed(weatherData.getWind().getSpeed());
        wind.setDegrees(weatherData.getWind().getDeg());

        response.setWind(wind);

        response.setHumidity(weatherData.getHumidity());
        response.setVisibility(weatherData.getVisibility());
        response.setSunrise(weatherData.getSunrise());
        response.setSunset(weatherData.getSunset());
        response.setDt(weatherData.getDt());
        response.setCityName(weatherData.getCityName());
        response.setCountry(weatherData.getCountry());
        response.setWeatherCode(weatherData.getCode());

        return response;
    }

    public HourlyResponse mapToHourlyResponse(HourlyWeatherResponse weatherData) {
        HourlyResponse response = new HourlyResponse();
        response.setCityName(weatherData.getCityName());
        response.setCountry(weatherData.getCountry());

        List<HourlyResponse.DailyWeather> dailyWeatherList = new ArrayList<>();
        for (com.example.generated.DailyWeather dailyWeather : weatherData.getDailyWeatherList()) {
            HourlyResponse.DailyWeather dailyWeatherMapped = new HourlyResponse.DailyWeather();
            dailyWeatherMapped.setDate(dailyWeather.getDate());

            List<HourlyResponse.HourlyWeather> hourlyWeatherList = new ArrayList<>();
            for (com.example.generated.HourlyWeather hourlyWeather : dailyWeather.getHourlyWeatherList()) {
                HourlyResponse.HourlyWeather hourlyWeatherMapped = new HourlyResponse.HourlyWeather();
                hourlyWeatherMapped.setDt(hourlyWeather.getDt());

                HourlyResponse.Main main = new HourlyResponse.Main();
                main.setTemp(hourlyWeather.getMain().getTemp());
                main.setFeelsLike(hourlyWeather.getMain().getFeelsLike());
                main.setTempMin(hourlyWeather.getMain().getTempMin());
                main.setTempMax(hourlyWeather.getMain().getTempMax());
                main.setPressure(hourlyWeather.getMain().getPressure());
                main.setHumidity(hourlyWeather.getMain().getHumidity());
                main.setTempKf(hourlyWeather.getMain().getTempKf());
                main.setSeaLevel(hourlyWeather.getMain().getSeaLevel());
                main.setGrndLevel(hourlyWeather.getMain().getGrndLevel());
                hourlyWeatherMapped.setMain(main);

                HourlyResponse.WeatherDescription weatherDescription = new HourlyResponse.WeatherDescription();
                weatherDescription.setId(hourlyWeather.getWeather().getId());
                weatherDescription.setMain(hourlyWeather.getWeather().getMain());
                weatherDescription.setDescription(hourlyWeather.getWeather().getDescription());
                weatherDescription.setIcon(hourlyWeather.getWeather().getIcon());
                hourlyWeatherMapped.setWeather(weatherDescription);

                HourlyResponse.Wind wind = new HourlyResponse.Wind();
                wind.setSpeed(hourlyWeather.getWind().getSpeed());
                wind.setDeg(hourlyWeather.getWind().getDeg());
                hourlyWeatherMapped.setWind(wind);

                hourlyWeatherMapped.setPop(hourlyWeather.getPop());
                if (hourlyWeather.getRain() != null) {
                    HourlyResponse.Rain rain = new HourlyResponse.Rain();
                    rain.setRain(hourlyWeather.getRain().getRain());
                    hourlyWeatherMapped.setRain(rain);
                }
                if (hourlyWeather.getSnow() != null) {
                    HourlyResponse.Snow snow = new HourlyResponse.Snow();
                    snow.setSnow(hourlyWeather.getSnow().getSnow());
                    hourlyWeatherMapped.setSnow(snow);
                }
                if (hourlyWeather.getClouds() != null) {
                    HourlyResponse.Clouds clouds = new HourlyResponse.Clouds();
                    clouds.setAll(hourlyWeather.getClouds().getAll());
                    hourlyWeatherMapped.setClouds(clouds);
                }
                if (hourlyWeather.getSys() != null) {
                    HourlyResponse.Sys sys = new HourlyResponse.Sys();
                    sys.setPod(hourlyWeather.getSys().getPod());
                    hourlyWeatherMapped.setSys(sys);
                }

                hourlyWeatherList.add(hourlyWeatherMapped);
            }

            dailyWeatherMapped.setHourlyWeather(hourlyWeatherList);
            dailyWeatherList.add(dailyWeatherMapped);
        }

        response.setDailyWeather(dailyWeatherList);
        return response;
    }

    public ForecastWeatherResponse mapToForecastResponse(ForecastResponse forecastData) {
        ForecastWeatherResponse response = new ForecastWeatherResponse();
        response.setCity(forecastData.getCity());
        response.setCountry(forecastData.getCountry());

        List<ForecastWeatherResponse.ForecastDay> forecastDayList = new ArrayList<>();
        for (com.example.generated.ForecastDay forecastDay : forecastData.getListList()) {
            ForecastWeatherResponse.ForecastDay forecastDayMapped = new ForecastWeatherResponse.ForecastDay();
            forecastDayMapped.setDt(forecastDay.getDt());
            forecastDayMapped.setSunrise(forecastDay.getSunrise());
            forecastDayMapped.setSunset(forecastDay.getSunset());

            ForecastWeatherResponse.Temp temp = new ForecastWeatherResponse.Temp();
            temp.setDay(forecastDay.getTemp().getDay());
            temp.setMin(forecastDay.getTemp().getMin());
            temp.setMax(forecastDay.getTemp().getMax());
            temp.setNight(forecastDay.getTemp().getNight());
            temp.setEve(forecastDay.getTemp().getEve());
            temp.setMorn(forecastDay.getTemp().getMorn());
            forecastDayMapped.setTemp(temp);

            ForecastWeatherResponse.FeelsLike feelsLike = new ForecastWeatherResponse.FeelsLike();
            feelsLike.setDay(forecastDay.getFeelsLike().getDay());
            feelsLike.setNight(forecastDay.getFeelsLike().getNight());
            feelsLike.setEve(forecastDay.getFeelsLike().getEve());
            feelsLike.setMorn(forecastDay.getFeelsLike().getMorn());
            forecastDayMapped.setFeelsLike(feelsLike);

            forecastDayMapped.setPressure(forecastDay.getPressure());
            forecastDayMapped.setHumidity(forecastDay.getHumidity());
            forecastDayMapped.setClouds(forecastDay.getClouds());
            forecastDayMapped.setRain(forecastDay.getRain());
            forecastDayMapped.setSnow(forecastDay.getSnow());

            List<ForecastWeatherResponse.WeatherDescription> weatherList = new ArrayList<>();
            for (com.example.generated.WeatherDescription weatherDesc : forecastDay.getWeatherList()) {
                ForecastWeatherResponse.WeatherDescription weatherMapped = new ForecastWeatherResponse.WeatherDescription();
                weatherMapped.setId(weatherDesc.getId());
                weatherMapped.setMain(weatherDesc.getMain());
                weatherMapped.setDescription(weatherDesc.getDescription());
                weatherMapped.setIcon(weatherDesc.getIcon());
                weatherList.add(weatherMapped);
            }
            forecastDayMapped.setWeather(weatherList);

            forecastDayList.add(forecastDayMapped);
        }

        response.setList(forecastDayList);
        return response;
    }
}
