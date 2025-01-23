package com.example.apigateway.Clients;

import com.example.apigateway.Client;
import com.example.generated.LocationServiceGrpc;
import io.grpc.Channel;

@Client(host = "${location.server.host}",
        port = "${location.server.port}")
public class LocationClient {
    private static LocationServiceGrpc.LocationServiceBlockingStub blockingStub;
    private static LocationServiceGrpc.LocationServiceStub stub;

    public static void init(Channel channel) {
        blockingStub = LocationServiceGrpc.newBlockingStub(channel);
        stub = LocationServiceGrpc.newStub(channel);
    }
}
