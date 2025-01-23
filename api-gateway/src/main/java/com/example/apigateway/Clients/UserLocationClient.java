package com.example.apigateway.Clients;

import com.example.apigateway.Client;
import com.example.generated.UserLocationGrpc;
import io.grpc.Channel;

@Client(host = "${user-location.server.host}",
        port = "${user-location.server.port}")
public class UserLocationClient {
    private static UserLocationGrpc.UserLocationBlockingStub blockingStub;

    public static void init(Channel channel) {
        blockingStub = UserLocationGrpc.newBlockingStub(channel);
    }
}
