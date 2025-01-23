package com.example.weather;

import com.example.weather.server.GrpcServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeatherApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherApplication.class, args);
    }

    @Bean
    public CommandLineRunner startGrpcServer(GrpcServer grpcServer) {
        return args -> grpcServer.start();
    }

}
