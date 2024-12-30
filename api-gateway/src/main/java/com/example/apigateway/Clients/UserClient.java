package com.example.apigateway.Clients;

import com.example.generated.*;
import io.grpc.Channel;


public class UserClient {
    private final UserServiceGrpc.UserServiceBlockingStub stub;

    UserClient(Channel channel) {
        stub = UserServiceGrpc.newBlockingStub(channel);
    }

    public ApiResponse login(String email, String password) {
        LoginUserRequest request = LoginUserRequest
                .newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build();

        return stub.login(request);
    }

    public ApiResponse register(String email, String password, String fullName) {
        RegisterUserRequest request = RegisterUserRequest
                .newBuilder()
                .setEmail(email)
                .setFullName(fullName)
                .setPassword(password)
                .build();

        return stub.register(request);
    }

    public ApiResponse delete(long id) {
        DeleteUserRequest request = DeleteUserRequest
                .newBuilder()
                .setId(id)
                .build();

        return stub.deleteUser(request);
    }
}