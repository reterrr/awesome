package com.example.location_j.servers;

import com.example.location_j.services.LocationService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.stereotype.Component;

@Component
public class GrpcServer {

    public final LocationService locationService;

    public GrpcServer(LocationService locationService) {
        this.locationService = locationService;
    }

    public void start() {
        try {
            Server server = ServerBuilder.
                    forPort(9090).
                    addService(locationService).build();

            server.start();
            System.out.println("Server started on port" + server.getPort());

            server.awaitTermination();
        } catch (Exception e) {
            System.err.println("Error starting server" + e.getMessage());
            e.printStackTrace();
        }
    }
}

