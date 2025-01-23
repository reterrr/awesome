package com.example.apigateway.Controllers;

import com.example.apigateway.Clients.WeatherClient;
import com.example.apigateway.Helpers.SecurityUtil;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController
{
    @GetMapping("/locations")
    public void locations() {

    }

    @DeleteMapping("/")
    public void deleteUser() {

    }
}
