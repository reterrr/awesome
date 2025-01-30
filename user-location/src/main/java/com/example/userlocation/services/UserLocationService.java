package com.example.userlocation.services;

import com.example.generated.*;
import com.example.userlocation.clients.LocationClient;
import com.example.userlocation.clients.WeatherClient;
import com.example.userlocation.dbevents.*;
import com.example.userlocation.domain.UserLocation;
import com.example.userlocation.domain.UserLocationId;
import com.example.userlocation.event.WeatherUpdateEventListener;
import com.example.userlocation.event.WeatherUpdateEventPublisher;
import com.example.userlocation.repositories.UserLocationRepository;
import com.google.common.eventbus.EventBus;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLocationService extends UserLocationGrpc.UserLocationImplBase {

    private final UserLocationRepository userLocationRepository;
    private final EventBus eventBus = new EventBus();
    private final WeatherUpdateEventPublisher weatherUpdateEventPublisher = new WeatherUpdateEventPublisher(eventBus);

    @Autowired
    public UserLocationService(UserLocationRepository userLocationRepository) {
        this.userLocationRepository = userLocationRepository;
        eventBus.register(new WeatherUpdateEventListener());
    }

    @Event(slug = "pin",
            type = DbEventType.POST,
            signaller = UnPinSignaller.class
    )
    @Override
    public void pin(PinRequest request, StreamObserver<PinResponse> responseObserver) {
        var userId = request.getUserId();
        var locationId = request.getLocationId();

        UserLocationId userLocationId = UserLocationId
                .builder()
                .location_id(locationId)
                .user_id(userId)
                .build();

        UserLocation userLocation = UserLocation
                .builder()
                .userLocationId(userLocationId)
                .build();

        userLocationRepository.save(userLocation);

        Iterable<Long> locationIds = userLocationRepository.findAllDistinctLocationIds();

        weatherUpdateEventPublisher.publishWeatherUpdateEvent(locationIds);

        var result = PinResponse
                .newBuilder()
                .setMessage("Success")
                .build();

        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Event(slug = "unpin",
            type = DbEventType.DELETE,
            signaller = UnPinSignaller.class
    )
    @Override
    public void unpin(UnpinRequest request, StreamObserver<UnPinResponse> responseObserver) {
        var userId = request.getUserId();
        var locationId = request.getLocationId();

        UserLocationId userLocationId = UserLocationId
                .builder()
                .location_id(locationId)
                .user_id(userId)
                .build();

        userLocationRepository.deleteById(userLocationId);

        Iterable<Long> locationIds = userLocationRepository.findAllDistinctLocationIds();

        weatherUpdateEventPublisher.publishWeatherUpdateEvent(locationIds);

        var result = UnPinResponse
                .newBuilder()
                .setMessage("Success")
                .build();

        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void getRelatedLocations(GetRelatedLocationsRequest request, StreamObserver<GetRelatedLocationsResponse> responseObserver) {
        var userId = request.getUserId();

        List<UserLocation> userLocations = userLocationRepository.findAllByUserId(userId);
        GetRelatedLocationsResponse.Builder response = GetRelatedLocationsResponse.newBuilder();

        for (UserLocation userLocation : userLocations) {
            response.addLocations(userLocation.getUserLocationId().getLocation_id());
        }

        var result = response.build();

        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void getRelatedUsers(GetRelatedUsersRequest request, StreamObserver<GetRelatedUsersResponse> responseObserver) {
        var locationId = request.getLocationId();

        List<UserLocation> userLocations = userLocationRepository.findAllByLocationId(locationId);
        GetRelatedUsersResponse.Builder response = GetRelatedUsersResponse.newBuilder();

        for (UserLocation userLocation : userLocations) {
            response.addUsers(userLocation.getUserLocationId().getUser_id());
        }

        var result = response.build();

        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }
}
