package com.example.apigateway.Controllers;

import com.example.apigateway.Clients.UserClient;
import com.example.apigateway.Requests.LoginRequest;
import com.example.apigateway.Requests.RegisterRequest;
import com.example.apigateway.Responses.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/auth")
@PermitAll
public class AuthController {
    @PermitAll
    @PostMapping("/login")
    public Response<String> login(@Validated @RequestBody LoginRequest request) {
        var response = UserClient.login(request.email, request.password);
        var apiDto = response.getApiResponse();

        return new Response<>(
                apiDto.getCode(),
                apiDto.getMessage(),
                response.getToken()
        );
    }

    @PermitAll
    @PostMapping("/register")
    public Response<Void> register(@Validated @RequestBody RegisterRequest request) {
        var apiDto = UserClient.register(request.getEmail(), request.getPassword(), request.getName());

        return new Response<>(
                apiDto.getCode(),
                apiDto.getMessage()
        );
    }
}
