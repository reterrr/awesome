package com.example.apigateway.Clients;

import com.example.apigateway.Client;
import com.example.generated.ApiResponse;
import com.example.generated.UserServiceGrpc;

@Client(host = "${location.server.host}",
        port = "${location.server.port}")
public class LocationClient {

    private static UserServiceGrpc.UserServiceBlockingStub stub;

    public static ApiResponse
}
