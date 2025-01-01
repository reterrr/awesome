package com.example.apigateway.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "email cant be empty")
    @Email(message = "email should be valid")
    public String email;

    @NotEmpty(message = "password cant be empty")
    public String password;
}
