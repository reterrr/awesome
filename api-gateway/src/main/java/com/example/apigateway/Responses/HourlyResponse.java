package com.example.apigateway.Responses;

import java.util.List;

public class HourlyResponse {

    private List<DailyWeather> dailyWeather;
    private String cityName;
    private String country;

    public List<DailyWeather> getDailyWeather() {
        return dailyWeather;
    }

    public void setDailyWeather(List<DailyWeather> dailyWeather) {
        this.dailyWeather = dailyWeather;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static class DailyWeather {
        private String date;
        private List<HourlyWeather> hourlyWeather;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<HourlyWeather> getHourlyWeather() {
            return hourlyWeather;
        }

        public void setHourlyWeather(List<HourlyWeather> hourlyWeather) {
            this.hourlyWeather = hourlyWeather;
        }
    }

    public static class HourlyWeather {
        private String dt;
        private Main main;
        private WeatherDescription weather;
        private Wind wind;
        private float pop;
        private Rain rain;
        private Clouds clouds;
        private Sys sys;
        private Snow snow;

        public String getDt() {
            return dt;
        }

        public void setDt(String dt) {
            this.dt = dt;
        }

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public WeatherDescription getWeather() {
            return weather;
        }

        public void setWeather(WeatherDescription weather) {
            this.weather = weather;
        }

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public float getPop() {
            return pop;
        }

        public void setPop(float pop) {
            this.pop = pop;
        }

        public Rain getRain() {
            return rain;
        }

        public void setRain(Rain rain) {
            this.rain = rain;
        }

        public Clouds getClouds() {
            return clouds;
        }

        public void setClouds(Clouds clouds) {
            this.clouds = clouds;
        }

        public Sys getSys() {
            return sys;
        }

        public void setSys(Sys sys) {
            this.sys = sys;
        }

        public Snow getSnow() {
            return snow;
        }

        public void setSnow(Snow snow) {
            this.snow = snow;
        }
    }

    public static class Main {
        private float temp;
        private float feelsLike;
        private float tempMin;
        private float tempMax;
        private int pressure;
        private int humidity;
        private float tempKf;
        private int seaLevel;
        private int grndLevel;

        public float getTemp() {
            return temp;
        }

        public void setTemp(float temp) {
            this.temp = temp;
        }

        public float getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(float feelsLike) {
            this.feelsLike = feelsLike;
        }

        public float getTempMin() {
            return tempMin;
        }

        public void setTempMin(float tempMin) {
            this.tempMin = tempMin;
        }

        public float getTempMax() {
            return tempMax;
        }

        public void setTempMax(float tempMax) {
            this.tempMax = tempMax;
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

        public float getTempKf() {
            return tempKf;
        }

        public void setTempKf(float tempKf) {
            this.tempKf = tempKf;
        }

        public int getSeaLevel() {
            return seaLevel;
        }

        public void setSeaLevel(int seaLevel) {
            this.seaLevel = seaLevel;
        }

        public int getGrndLevel() {
            return grndLevel;
        }

        public void setGrndLevel(int grndLevel) {
            this.grndLevel = grndLevel;
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

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public static class Wind {
        private float speed;
        private int deg;

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public int getDeg() {
            return deg;
        }

        public void setDeg(int deg) {
            this.deg = deg;
        }
    }

    public static class Rain {
        private float rain;

        public float getRain() {
            return rain;
        }

        public void setRain(float rain) {
            this.rain = rain;
        }
    }

    public static class Snow {
        private float snow;

        public float getSnow() {
            return snow;
        }

        public void setSnow(float snow) {
            this.snow = snow;
        }
    }

    public static class Clouds {
        private int all;

        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }
    }

    public static class Sys {
        private String pod;

        public String getPod() {
            return pod;
        }

        public void setPod(String pod) {
            this.pod = pod;
        }
    }
}