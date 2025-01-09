package com.example.weather.server;

import com.example.weather.controllers.WeatherService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.stereotype.Component;

@Component
public class GrpcServer {

    public final WeatherService weatherService;

    public GrpcServer(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public void start() {
        try{
            Server server = ServerBuilder.
                    forPort(9090).
                    addService(weatherService).build();

            server.start();
            System.out.println("Server started on port" + server.getPort());

            server.awaitTermination();
        }catch (Exception e){
            System.err.println("Error starting server" + e.getMessage());
            e.printStackTrace();
        }
    }
}
