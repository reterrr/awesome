package com.example.apigateway.Controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
public class LocationController {
    @GetMapping("/search")
    public void search() {

    }

    @GetMapping("/{id}")
    public void get() {

    }

    @PostMapping("/{id}")
    public void pin(@PathVariable String id) {

    }

    @DeleteMapping("/{id}")
    public void unpin(@PathVariable String id) {

    }
}
