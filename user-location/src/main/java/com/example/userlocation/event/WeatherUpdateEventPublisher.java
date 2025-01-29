package com.example.userlocation.event;

import com.google.common.eventbus.EventBus;

public class WeatherUpdateEventPublisher {

    private final EventBus eventBus;

    public WeatherUpdateEventPublisher(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void publishWeatherUpdateEvent(Iterable<Long> locationIds) {
        WeatherUpdateEvent event = new WeatherUpdateEvent(locationIds);
        eventBus.post(event);
    }
}
