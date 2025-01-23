package com.example.apigateway.Clients;

import com.example.apigateway.Client;
import com.example.apigateway.Responses.HourlyResponse;
import com.example.generated.*;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Client(host = "${weather.server.host}",
        port = "${weather.server.port}")
public class WeatherClient {
    private static WeatherServiceGrpc.WeatherServiceStub stub;

    public static CurrentWeatherResponse getCurrentWeather(String city) throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);

        final CurrentWeatherResponse[] currentWeatherResponse = new CurrentWeatherResponse[1];

        StreamObserver<CurrentWeatherResponse> responseObserver =
                new StreamObserver<CurrentWeatherResponse>() {
                    @Override
                    public void onNext(CurrentWeatherResponse response) {

                        System.out.println("Current weather response: " + response);
                        currentWeatherResponse[0] = response;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.err.println("Error occurred: " + throwable.getMessage());
                        finishLatch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Weather data stream completed.");
                        finishLatch.countDown();
                    }
                };

        StreamObserver<CurrentWeatherRequest> requestObserver = stub.getCurrentWeather(responseObserver);

        try{
            CurrentWeatherRequest request = CurrentWeatherRequest.newBuilder()
                    .setCity(city)
                    .build();
            requestObserver.onNext(request);
        }catch (Exception e){
            requestObserver.onError(e);
        }
        requestObserver.onCompleted();


        finishLatch.await(1, TimeUnit.MINUTES);

        return currentWeatherResponse[0];
    }

    public static HourlyWeatherResponse getHourlyWeather(String city) throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);

        final HourlyWeatherResponse[] hourlyWeatherResponse = new HourlyWeatherResponse[1];

        StreamObserver<HourlyWeatherResponse> responseObserver =
                new StreamObserver<HourlyWeatherResponse>() {
                    @Override
                    public void onNext(HourlyWeatherResponse response) {

                        System.out.println("Current weather response: " + response);
                        hourlyWeatherResponse[0] = response;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.err.println("Error occurred: " + throwable.getMessage());
                        finishLatch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Weather data stream completed.");
                        finishLatch.countDown();
                    }
                };

        StreamObserver<HourlyWeatherRequest> requestObserver = stub.getHourlyWeather(responseObserver);

        try{
            HourlyWeatherRequest request = HourlyWeatherRequest.newBuilder()
                    .setCity(city)
                    .build();
            requestObserver.onNext(request);
        }catch (Exception e){
            requestObserver.onError(e);
        }
        requestObserver.onCompleted();


        finishLatch.await(1, TimeUnit.MINUTES);

        return hourlyWeatherResponse[0];
    }

    public static ForecastResponse getForecastWeather(String city) throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);

        final ForecastResponse[] forecastWeatherResponse = new ForecastResponse[1];

        StreamObserver<ForecastResponse> responseObserver =
                new StreamObserver<ForecastResponse>() {
                    @Override
                    public void onNext(ForecastResponse response) {

                        System.out.println("Current weather response: " + response);
                        forecastWeatherResponse[0] = response;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.err.println("Error occurred: " + throwable.getMessage());
                        finishLatch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Weather data stream completed.");
                        finishLatch.countDown();
                    }
                };

        StreamObserver<ForecastRequest> requestObserver = stub.getForecastWeather(responseObserver);

        try{
            ForecastRequest request = ForecastRequest.newBuilder()
                    .setCity(city)
                    .build();
            requestObserver.onNext(request);
        }catch (Exception e){
            requestObserver.onError(e);
        }
        requestObserver.onCompleted();


        finishLatch.await(1, TimeUnit.MINUTES);

        return forecastWeatherResponse[0];
    }

    public static void init(Channel channel) {
        stub = WeatherServiceGrpc.newStub(channel);
    }


}
