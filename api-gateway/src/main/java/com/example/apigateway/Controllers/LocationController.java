package com.example.apigateway.Controllers;

import com.example.apigateway.Clients.LocationClient;
import com.example.apigateway.Clients.UserLocationClient;
import com.example.apigateway.Helpers.SecurityUtil;
import com.example.apigateway.Responses.Response;
import com.example.apigateway.RestLocation;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/locations")
public class LocationController {
    @GetMapping("/search-stream")
    public SseEmitter searchStream(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "longitude", required = false) double longitude,
            @RequestParam(value = "latitude", required = false) double latitude) {

        SseEmitter emitter = new SseEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                LocationClient.search(name, country, latitude, longitude, emitter);
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });

        emitter.onCompletion(executor::shutdown);
        emitter.onTimeout(executor::shutdown);

        return emitter;
    }

    @GetMapping("/{id}")
    public long get(@PathVariable int id) {
        return LocationClient.getLocation(id);
    }

    @GetMapping("/gets")
    public List<String> gets(@RequestBody List<Long> ids) {
        return LocationClient.getLocations(ids);
    }

    @PostMapping("/{id}")
    public Response<Void> pin(@PathVariable int id) {
        if (!LocationClient.exists(id))
            return new Response<>(
                    400,
                    "No such location"
            );

        var response = UserLocationClient.pin(Long.parseLong(SecurityUtil.getCurrentUserId()), id);

        var message = response.getMessage();

        return new Response<>(
                200,
                message
        );
    }

    @DeleteMapping("/{id}")
    public Response<Void> unpin(@PathVariable int id) {
        if (!LocationClient.exists(id))
            return new Response<>(
                    400,
                    "No such location"
            );

        var response = UserLocationClient.unpin(Long.parseLong(SecurityUtil.getCurrentUserId()), id);

        var message = response.getMessage();

        return new Response<>(
                200,
                message
        );
    }
}
