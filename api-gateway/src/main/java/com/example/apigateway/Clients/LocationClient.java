package com.example.apigateway.Clients;

import com.example.apigateway.Client;
import com.example.generated.*;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;

import java.util.List;

@Client(host = "${location.server.host}",
        port = "${location.server.port}")
public class LocationClient {
    private static LocationServiceGrpc.LocationServiceBlockingStub blockingStub;
    private static LocationServiceGrpc.LocationServiceStub stub;

    public static Long getLocation(int id) {
        var request = GetLocationRequest
                .newBuilder()
                .setId(id)
                .build();

        return blockingStub
                .get(request)
                .getLocation()
                .getId();
    }

    public static List<String> getLocations(Iterable<Long> ids) {
        var request = GetLocationsRequest
                .newBuilder()
                .addAllIds(ids)
                .build();

        return blockingStub.getLocations(request)
                .getLocationsList()
                .stream()
                .map(Location::getName)
                .toList();
    }

    public static void search() {
        //
    }

    public static void init(Channel channel) {
        blockingStub = LocationServiceGrpc.newBlockingStub(channel);
        stub = LocationServiceGrpc.newStub(channel);
    }
}
