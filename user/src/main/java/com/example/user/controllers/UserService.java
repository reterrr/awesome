package com.example.user.controllers;

import com.example.generated.*;
import com.example.user.domain.User;
import com.example.user.helpers.JwtUtil;
import com.example.user.helpers.Regex;
import com.example.user.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Regex regex;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void register(RegisterUserRequest request, StreamObserver<ApiResponse> responseObserver) {
        String fullName = request.getFullName();
        String password = request.getPassword();
        String email = request.getEmail();

        if(fullName.isBlank() || password.isBlank() || email.isBlank() ) {
            ApiResponse response = ApiResponse.newBuilder().
                    setCode(400).
                    setMessage("Fields cannot be empty").
                    build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        if(!regex.isPasswordStrong(password)){
            ApiResponse response = ApiResponse.newBuilder().
                    setCode(400).
                    setMessage("Password must be at least 8 characters long, contain at least one letter, one number, and one special character.").
                    build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        if(userRepository.existsByEmail(email)) {
            ApiResponse response = ApiResponse.newBuilder().
                    setCode(409).
                    setMessage("Email already exists").
                    build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setFullName(fullName);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        userRepository.save(user);

        ApiResponse response = ApiResponse.newBuilder().
                setCode(201).
                setMessage("User created successfully").
                build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void login(LoginUserRequest request, StreamObserver<LoginUserResponse> responseObserver){
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email);
        if (user == null) {
            ApiResponse apiResponse = ApiResponse.newBuilder()
                    .setCode(401)  // 401 Unauthorized
                    .setMessage("User not found")
                    .build();

            LoginUserResponse loginUserResponse = LoginUserResponse.newBuilder().
                    setApiResponse(apiResponse).
                    build();
            responseObserver.onNext(loginUserResponse);
            responseObserver.onCompleted();
            return;
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            ApiResponse apiResponse = ApiResponse.newBuilder()
                    .setCode(401)  // 401 Unauthorized
                    .setMessage("Password does not match")
                    .build();

            LoginUserResponse loginUserResponse = LoginUserResponse.newBuilder().
                    setApiResponse(apiResponse).
                    build();
            responseObserver.onNext(loginUserResponse);
            responseObserver.onCompleted();
            return;
        }

        String token = jwtUtil.generateJWT(userRepository.getUserIdByEmail(email).toString());

        jwtUtil.extractUserId(token);

        ApiResponse apiResponse = ApiResponse.newBuilder()
                .setCode(200)
                .setMessage("Login successful")
                .build();

        LoginUserResponse loginUserResponse = LoginUserResponse.newBuilder()
                .setToken(token)
                .setApiResponse(apiResponse)
                .build();
        responseObserver.onNext(loginUserResponse);
        responseObserver.onCompleted();
    }

    @Transactional
    @Override
    public void deleteUser( DeleteUserRequest request, StreamObserver<ApiResponse> responseObserver){
        long id = request.getId();

        User user = userRepository.findById(id);
        if (user == null){
            ApiResponse response = ApiResponse.newBuilder()
                    .setCode(401)
                    .setMessage("User not found")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        userRepository.deleteById(id);

        ApiResponse response = ApiResponse.newBuilder()
                .setCode(200)
                .setMessage("User deleted")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
