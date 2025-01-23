package com.example.userlocation.clients;

import com.example.generated.UpdateLocationsRequest;
import com.example.generated.WeatherServiceGrpc;
import com.example.userlocation.Client;
import io.grpc.Channel;

@Client(host = "${weather.server.host}",
        port = "${weather.server.port}")
public class WeatherClient {
    private static WeatherServiceGrpc.WeatherServiceBlockingStub stub;

    public static void updateLocations(Iterable<String> names) {
        var request = UpdateLocationsRequest
                .newBuilder()
                .addAllNames(names)
                .build();

        stub.updateLocations(request);
    }

    public static void init(Channel channel) {
        stub = WeatherServiceGrpc.newBlockingStub(channel);
    }
}
