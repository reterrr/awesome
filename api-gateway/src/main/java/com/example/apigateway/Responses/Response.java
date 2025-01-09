package com.example.apigateway.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {
    private int code;
    private String message;
    private T data;

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }
}
