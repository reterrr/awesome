package com.example.apigateway.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {
    @Email
    private String email;

    private String password;

    private String confirmPassword;

    @NotEmpty
    private String name;
}
