package com.example.user.helpers;

import org.springframework.context.annotation.Configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class Regex {
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>\\[\\]_/+=-]).{8,}$";

    public boolean isPasswordStrong(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        System.out.println(matcher.matches());
        return matcher.matches();
    }
}

