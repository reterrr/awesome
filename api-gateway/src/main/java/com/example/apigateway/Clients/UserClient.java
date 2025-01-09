package com.example.apigateway.Clients;

import com.example.apigateway.Client;
import com.example.generated.*;
import io.grpc.Channel;

@Client(host = "${user.server.host}",
        port = "${user.server.port}")
public class UserClient {
    private static UserServiceGrpc.UserServiceBlockingStub stub;

    public static LoginUserResponse login(String email, String password) {
        LoginUserRequest request = LoginUserRequest
                .newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build();

        return stub.login(request);
    }

    public static ApiResponse register(String email, String password, String fullName) {
        RegisterUserRequest request = RegisterUserRequest
                .newBuilder()
                .setEmail(email)
                .setFullName(fullName)
                .setPassword(password)
                .build();

        return stub.register(request);
    }

    public static ApiResponse delete(int id) {
        DeleteUserRequest request = DeleteUserRequest
                .newBuilder()
                .setId(id)
                .build();

        return stub.deleteUser(request);
    }

    public static void init(Channel channel) {
        stub = UserServiceGrpc.newBlockingStub(channel);
    }
}