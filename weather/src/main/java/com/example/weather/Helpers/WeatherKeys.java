package com.example.weather.Helpers;

public enum WeatherKeys{
    WEATHER("weather : "),
    HOURLY("hourly : "),
    DAILY("daily : ");

    private final String label;


    private WeatherKeys(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}