package com.example.userlocation.clients;

import com.example.generated.GetLocationsRequest;
import com.example.generated.Location;
import com.example.generated.LocationServiceGrpc;
import com.example.generated.LocationsResponse;
import com.example.userlocation.Client;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.channel.EventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.socket.nio.NioSocketChannel;
import io.grpc.netty.shaded.io.netty.util.concurrent.DefaultThreadFactory;


import java.util.List;

@Client(host = "${location.server.host}",
        port = "${location.server.port}")
public class LocationClient {
    private static LocationServiceGrpc.LocationServiceBlockingStub stub;

    public static Iterable<String> getLocations(Iterable<Long> ids) {
        var request = GetLocationsRequest.newBuilder()
                .addAllIds(ids)
                .build();

        var locations = stub.getLocations(request).getLocationsList();

        return locations.stream()
                .map(Location::getName)
                .toList();
    }

    public static void init(Channel channel) {
        stub = LocationServiceGrpc.newBlockingStub(channel);
    }
}
