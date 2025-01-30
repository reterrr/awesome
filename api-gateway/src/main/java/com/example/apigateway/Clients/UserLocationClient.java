package com.example.apigateway.Clients;

import com.example.apigateway.Client;
import com.example.generated.*;
import io.grpc.Channel;

@Client(host = "${user-location.server.host}",
        port = "${user-location.server.port}")
public class UserLocationClient {
    private static UserLocationGrpc.UserLocationBlockingStub blockingStub;

    public static PinResponse pin(long userId, long locationId) {
        var pinRequest = PinRequest
                .newBuilder()
                .setUserId(userId)
                .setLocationId(locationId)
                .build();

        return blockingStub.pin(pinRequest);
    }

    public static UnPinResponse unpin(long userId, long locationId) {
        var unpinRequest = UnpinRequest
                .newBuilder()
                .setUserId(userId)
                .setLocationId(locationId)
                .build();

        return blockingStub.unpin(unpinRequest);
    }

    public static GetRelatedLocationsResponse getRelatedLocations(long userId) {
        var getRelatedLocationsRequest = GetRelatedLocationsRequest
                .newBuilder()
                .setUserId(userId)
                .build();

        return blockingStub.getRelatedLocations(getRelatedLocationsRequest);
    }

    public static void init(Channel channel) {
        blockingStub = UserLocationGrpc.newBlockingStub(channel);
    }
}
