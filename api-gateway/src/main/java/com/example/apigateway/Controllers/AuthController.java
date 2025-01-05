package com.example.apigateway.Controllers;

import com.example.apigateway.Clients.UserClient;
import com.example.apigateway.Requests.LoginRequest;
import com.example.apigateway.Requests.RegisterRequest;
import com.example.generated.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/auth")
@PermitAll
public class AuthController {
    @PermitAll
    @PostMapping("/login")
    public ApiResponse login(@Validated @RequestBody LoginRequest request) {
        return UserClient.login(request.email, request.password);
    }

    @PermitAll
    @PostMapping("/register")
    public ApiResponse register(@Validated @RequestBody RegisterRequest request) {
        return UserClient.register(request.getEmail(),request.getPassword(), request.getName());
    }
}
