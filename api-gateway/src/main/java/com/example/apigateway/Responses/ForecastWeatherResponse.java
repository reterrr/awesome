package com.example.apigateway.Responses;

import java.util.ArrayList;
import java.util.List;

public class ForecastWeatherResponse {
    private String city;
    private String country;
    private List<ForecastDay> list = new ArrayList<>();;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<ForecastDay> getList() {
        return list;
    }

    public void setList(List<ForecastDay> list) {
        this.list = list;
    }

    public static class ForecastDay {
        private String dt;
        private String sunrise;
        private String sunset;
        private Temp temp;
        private FeelsLike feelsLike;
        private int pressure;
        private int humidity;
        private List<WeatherDescription> weather = new ArrayList<>();;
        private int clouds;
        private float rain;
        private float snow;

        public String getDt() {
            return dt;
        }

        public void setDt(String dt) {
            this.dt = dt;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public Temp getTemp() {
            return temp;
        }

        public void setTemp(Temp temp) {
            this.temp = temp;
        }

        public FeelsLike getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(FeelsLike feelsLike) {
            this.feelsLike = feelsLike;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public List<WeatherDescription> getWeather() {
            return weather;
        }

        public void setWeather(List<WeatherDescription> weather) {
            this.weather = weather;
        }

        public int getClouds() {
            return clouds;
        }

        public void setClouds(int clouds) {
            this.clouds = clouds;
        }

        public float getRain() {
            return rain;
        }

        public void setRain(float rain) {
            this.rain = rain;
        }

        public float getSnow() {
            return snow;
        }

        public void setSnow(float snow) {
            this.snow = snow;
        }

    }

    public static class Temp {
        private float day;
        private float min;
        private float max;
        private float night;
        private float eve;
        private float morn;

        public float getDay() {
            return day;
        }

        public void setDay(float day) {
            this.day = day;
        }

        public float getMin() {
            return min;
        }

        public void setMin(float min) {
            this.min = min;
        }

        public float getMax() {
            return max;
        }

        public void setMax(float max) {
            this.max = max;
        }

        public float getNight() {
            return night;
        }

        public void setNight(float night) {
            this.night = night;
        }

        public float getMorn() {
            return morn;
        }

        public void setMorn(float morn) {
            this.morn = morn;
        }

        public float getEve() {
            return eve;
        }

        public void setEve(float eve) {
            this.eve = eve;
        }

    }

    public static class FeelsLike {
        private float day;
        private float night;
        private float eve;
        private float morn;

        public float getDay() {
            return day;
        }

        public void setDay(float day) {
            this.day = day;
        }

        public float getNight() {
            return night;
        }

        public void setNight(float night) {
            this.night = night;
        }

        public float getMorn() {
            return morn;
        }

        public void setMorn(float morn) {
            this.morn = morn;
        }

        public float getEve() {
            return eve;
        }

        public void setEve(float eve) {
            this.eve = eve;
        }

    }

    public static class WeatherDescription {
        private int id;
        private String main;
        private String description;
        private String icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

    }
}
