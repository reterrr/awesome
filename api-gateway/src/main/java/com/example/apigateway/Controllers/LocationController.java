package com.example.apigateway.Controllers;

import com.example.apigateway.Clients.LocationClient;
import com.example.apigateway.Helpers.SecurityUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {
    @GetMapping("/search")
    public void search() {
        //
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
    public void pin(@PathVariable String id) {
        //
    }

    @DeleteMapping("/{id}")
    public void unpin(@PathVariable String id) {
        //
    }
}
