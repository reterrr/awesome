package com.example.apigateway.Helpers;

import com.example.apigateway.Requests.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RegisterRequest> {
    @Override
    public boolean isValid(RegisterRequest registerRequest, ConstraintValidatorContext constraintValidatorContext) {
        String password = registerRequest.getPassword();
        String confirmPassword = registerRequest.getConfirmPassword();

        return password.equals(confirmPassword);
    }
}
