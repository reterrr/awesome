package com.example.apigateway.Responses;


public class WeatherResponse {
    private CurrentWeather currentWeather;
    private Wind wind;
    private int humidity;
    private int visibility;
    private String sunrise;
    private String sunset;
    private String dt;
    private String city_name;
    private String country;
    private int code;

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setCityName(String city_name) {
        this.city_name = city_name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public void setWeatherCode(int weatherCode) {
        this.code = weatherCode;
    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public Wind getWind() {
        return wind;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getVisibility() {
        return visibility;
    }

    public String getSunset() {
        return sunset;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getDt() {
        return dt;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getCountry() {
        return country;
    }

    public int getCode() {
        return code;
    }

    public static class CurrentWeather{
        private float temperature;
        private String description;
        private String icon;
        private float feels_like;
        private float temperature_min;
        private float temperature_max;
        private String main;

        public void setDescription(String description) {
            this.description = description;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setFeels_like(float feels_like) {
            this.feels_like = feels_like;
        }

        public void setTemperature_min(float temperature_min) {
            this.temperature_min = temperature_min;
        }


        public void setTemperature_max(float temperature_max) {
            this.temperature_max = temperature_max;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public float getTemperature() {
            return temperature;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

        public float getFeels_like() {
            return feels_like;
        }

        public float getTemperature_min() {
            return temperature_min;
        }

        public float getTemperature_max() {
            return temperature_max;
        }
    }

    public static class Wind{
        private float speed;
        private int degrees;


        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public void setDegrees(int degrees) {
            this.degrees = degrees;
        }



        public float getSpeed() {
            return speed;
        }

        public int getDegrees() {
            return degrees;
        }

    }
}


