package com.example.user.server;

import com.example.user.controllers.UserService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Component
public class GrpcServer {

    public final UserService userService;

    public GrpcServer(UserService userService) {
        this.userService = userService;
    }

    public void start() {
        try{
            Server server = ServerBuilder.
                    forPort(9090).
                    addService(userService).build();

            server.start();
            System.out.println("Server started on port" + server.getPort());

            server.awaitTermination();
        }catch (Exception e){
            System.err.println("Error starting server" + e.getMessage());
            e.printStackTrace();
        }
    }

}

