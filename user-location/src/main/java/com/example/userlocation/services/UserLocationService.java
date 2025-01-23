package com.example.userlocation.services;

import com.example.generated.*;
import com.example.userlocation.clients.LocationClient;
import com.example.userlocation.clients.WeatherClient;
import com.example.userlocation.dbevents.*;
import com.example.userlocation.domain.UserLocation;
import com.example.userlocation.domain.UserLocationId;
import com.example.userlocation.repositories.UserLocationRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLocationService extends UserLocationGrpc.UserLocationImplBase {

    private final UserLocationRepository userLocationRepository;

    @Autowired
    public UserLocationService(UserLocationRepository userLocationRepository) {
        this.userLocationRepository = userLocationRepository;
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

        WeatherClient.updateLocations(LocationClient.getLocations(locationIds));

//        UnPinSignaller signaller = new UnPinSignaller();
//        signaller.on();

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

//        UnPinSignaller signaller = new UnPinSignaller();
//        signaller.on();

        userLocationRepository.deleteById(userLocationId);

        Iterable<Long> locationIds = userLocationRepository.findAllDistinctLocationIds();
        locationIds.forEach(System.out::println);

        WeatherClient.updateLocations(LocationClient.getLocations(locationIds));

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

        List<UserLocation> userLocations = userLocationRepository.findAll();
        GetRelatedLocationsResponse.Builder response = GetRelatedLocationsResponse.newBuilder();

        for (UserLocation userLocation : userLocations) {
            if (userLocation.getUserLocationId().getLocation_id() == userId) {
                response.addLocations(userLocation.getUserLocationId().getLocation_id());
            }
        }

        var result = response.build();

        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void getRelatedUsers(GetRelatedUsersRequest request, StreamObserver<GetRelatedUsersResponse> responseObserver) {
        var locationId = request.getLocationId();

        List<UserLocation> userLocations = userLocationRepository.findAll();
        GetRelatedUsersResponse.Builder response = GetRelatedUsersResponse.newBuilder();

        for (UserLocation userLocation : userLocations) {
            if (userLocation.getUserLocationId().getLocation_id() == locationId) {
                response.addUsers(userLocation.getUserLocationId().getUser_id());
            }
        }

        var result = response.build();

        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }
}
