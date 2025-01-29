package com.example.userlocation.event;

import com.example.userlocation.clients.LocationClient;
import com.example.userlocation.clients.WeatherClient;
import com.google.common.eventbus.Subscribe;

import java.util.concurrent.CompletableFuture;

public class WeatherUpdateEventListener {
    @Subscribe
    public void onWeatherUpdateEvent(WeatherUpdateEvent event) {

        CompletableFuture.runAsync(() ->
                WeatherClient.updateLocations(
                        LocationClient.getLocations(
                                event.locationIds()
                        )
                )
        );
    }
}
