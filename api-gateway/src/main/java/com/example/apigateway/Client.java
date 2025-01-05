package com.example.apigateway;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Client {
    String host() default "localhost";
    String port() default "8080";
}
