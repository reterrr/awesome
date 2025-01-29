package com.example.userlocation.event;

public record WeatherUpdateEvent(Iterable<Long> locationIds) {}
