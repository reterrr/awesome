package com.example.apigateway.Clients;

import com.example.apigateway.Client;
import com.example.apigateway.Responses.Response;
import com.example.apigateway.RestLocation;
import com.example.generated.*;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Client(host = "${location.server.host}",
        port = "${location.server.port}")
public class LocationClient {
    private static LocationServiceGrpc.LocationServiceBlockingStub blockingStub;
    private static LocationServiceGrpc.LocationServiceStub stub;

    public static class SseBuffer {
        private static final CopyOnWriteArrayList<Consumer<Response<List<RestLocation>>>> listeners = new CopyOnWriteArrayList<>();

        public static void addListener(Consumer<Response<List<RestLocation>>> listener) {
            listeners.add(listener);
        }

        public static void removeListener(Consumer<Response<List<RestLocation>>> listener) {
            listeners.remove(listener);
        }
    }

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

    public static List<RestLocation> getLocations(Iterable<Long> ids) {
        var request = GetLocationsRequest
                .newBuilder()
                .addAllIds(ids)
                .build();

        return blockingStub.getLocations(request)
                .getLocationsList()
                .stream()
                .map(loc -> RestLocation.builder()
                        .id(loc.getId())
                        .name(loc.getName())
                        .country(loc.getCountry())
                        .latitude(loc.getLatitude())
                        .longitude(loc.getLongitude())
                        .build())
                .toList();
    }

    public static boolean exists(int id) {
        var request = ExistsLocationRequest
                .newBuilder()
                .setId(id)
                .build();

        var response = blockingStub.exists(request);

        return response.getExists();
    }

    public static void search(String name, String country, double latitude, double longitude, SseEmitter emitter) {
        StreamObserver<SearchLocationRequest> requestObserver = stub.search(new StreamObserver<>() {
            @Override
            public void onNext(LocationsResponse locationsResponse) {
                List<RestLocation> restLocations = locationsResponse.getLocationsList().stream()
                        .map(loc -> RestLocation.builder()
                                .id(loc.getId())
                                .name(loc.getName())
                                .country(loc.getCountry())
                                .latitude(loc.getLatitude())
                                .longitude(loc.getLongitude())
                                .build())
                        .toList();

                System.out.println("Found " + restLocations.size() + " rest locations\n");

                try {
                    emitter.send(SseEmitter.event()
                            .data(new Response<>(200, "Streaming location data", restLocations))
                            .id(String.valueOf(System.nanoTime()))
                            .name("location-update"));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.completeWithError(throwable);
            }

            @Override
            public void onCompleted() {
                emitter.complete();
            }
        });

        requestObserver.onNext(
                SearchLocationRequest.newBuilder()
                        .setPatternName(name)
                        .setPatternCountry(country)
                        .setPatternLatitude(latitude)
                        .setPatternLongitude(longitude)
                        .build()
        );

        requestObserver.onCompleted();
    }


    public static void init(Channel channel) {
        blockingStub = LocationServiceGrpc.newBlockingStub(channel);
        stub = LocationServiceGrpc.newStub(channel);
    }
}
