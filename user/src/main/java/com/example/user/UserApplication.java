package com.example.user;

import com.example.user.controllers.UserService;
import com.example.user.repositories.UserRepository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserApplication{

    private final UserRepository userRepository;

    public UserApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Bean
    public CommandLineRunner startGrpcServer(UserService userService) {
        return args -> {
            System.out.println("Starting gRPC server...");
            try {
                // Start the gRPC server on port 9090
                Server server = ServerBuilder.forPort(9090) // gRPC server port
                        .addService(userService) // Add the gRPC service
                        .build();

                // Start the server
                server.start();
                System.out.println("gRPC server started on port 9090");

                // Keep the server running
                server.awaitTermination();
            } catch (Exception e) {
                System.err.println("Error starting gRPC server: " + e.getMessage());
                e.printStackTrace();
            }
        };
//    @Override
//    public void run(String... args) throws Exception {
//        userRepository.findAll().forEach(user -> {
//            System.out.println(user.toString());
//        });
    }
}
