package com.example.apigateway.Controllers;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController
{
    @GetMapping("/locations")
    public void locations() {

    }

    @GetMapping("/locations/search")
    public void locationsSearch(@RequestParam String query) {

    }

    @DeleteMapping("/locations/{id}")
    public void unpinLocation(@PathVariable String id) {

    }

}
