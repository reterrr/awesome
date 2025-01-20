package com.example.userlocation.servers;

import com.example.generated.*;
import com.example.userlocation.services.UserLocationService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrpcServer extends UserLocationGrpc.UserLocationImplBase {
    private final UserLocationService userLocationService;

    @Autowired
    public GrpcServer(UserLocationService userLocationService) {
        this.userLocationService = userLocationService;
    }

    public void start() {
        try {
            Server server = ServerBuilder
                    .forPort(9090)
                    .addService(userLocationService)
                    .build();

            server.start();
            System.out.println("Server started on port" + server.getPort());

            server.awaitTermination();
        } catch (Exception e) {
            System.err.println("Error starting server" + e.getMessage());
            e.printStackTrace();
        }
    }

}
