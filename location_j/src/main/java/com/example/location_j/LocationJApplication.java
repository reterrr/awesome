package com.example.location_j;

import com.example.location_j.servers.GrpcServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LocationJApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocationJApplication.class, args);
    }

    @Bean
    public CommandLineRunner startGrpcServer(GrpcServer grpcServer) {
        return args -> grpcServer.start();
    }
}
