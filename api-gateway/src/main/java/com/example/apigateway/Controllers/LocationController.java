package com.example.apigateway.Controllers;

import com.example.apigateway.Clients.LocationClient;
import com.example.apigateway.Clients.UserLocationClient;
import com.example.apigateway.Helpers.SecurityUtil;
import com.example.apigateway.Responses.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {
    @GetMapping("/search")
    public void search() {
        //TODO
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
    public Response<Void> pin(@PathVariable long id) {
        var response = UserLocationClient.pin(Long.parseLong(SecurityUtil.getCurrentUserId()), id);

        var message = response.getMessage();

        return new Response<>(200, message);
    }

    @DeleteMapping("/{id}")
    public void unpin(@PathVariable long id) {
        //TODO
    }
}
