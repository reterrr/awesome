package com.example.user.controllers;

import com.example.generated.ApiResponse;
import com.example.generated.LoginUserRequest;
import com.example.generated.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void login(LoginUserRequest request, StreamObserver<ApiResponse> responseObserver) {
        String email = request.getEmail();
        String password = request.getPassword();

        ApiResponse response = ApiResponse.newBuilder()
                .setCode(200)
                .setMessage("Login successful for user: " + email)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
