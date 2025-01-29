package com.example.location_j.services;

import com.example.generated.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import io.grpc.stub.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LocationService extends LocationServiceGrpc.LocationServiceImplBase {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LocationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void get(GetLocationRequest request, StreamObserver<GetLocationResponse> responseObserver) {
        long id = request.getId();
        String query = "SELECT * FROM cities WHERE id = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query, id);

        if (result.isEmpty()) {
            responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND.withDescription("Location not found")));
            return;
        }

        Map<String, Object> row = result.get(0);
        Location location = Location.newBuilder()
                .setId(id)
                .setCountry((String) row.get("country"))
                .setName((String) row.get("name"))
                .setLatitude((Float)row.get("lat"))
                .setLongitude((Float)row.get("lon"))
                .build();

        GetLocationResponse response = GetLocationResponse.newBuilder()
                .setLocation(location)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void exists(ExistsLocationRequest request, StreamObserver<ExistsLocationResponse> responseObserver) {
        long id = request.getId();

        String query = "SELECT EXISTS(SELECT 1 FROM cities WHERE id = ?)";

        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(query, id);

            boolean exists = false;
            if (!result.isEmpty()) {
                Map<String, Object> row = result.get(0);
                exists = (Boolean) row.get("exists");
            }

            ExistsLocationResponse response = ExistsLocationResponse.newBuilder()
                    .setExists(exists)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (DataAccessException e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Database error").withCause(e).asRuntimeException());
        }
    }


    @Override
    public void getLocations(GetLocationsRequest request, StreamObserver<LocationsResponse> responseObserver) {
        List<Long> ids = request.getIdsList();
        String idString = "(" + String.join(",", ids.stream().map(String::valueOf).collect(Collectors.toList())) + ")";
        String query = "SELECT * FROM cities WHERE id IN " + idString;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        LocationsResponse.Builder responseBuilder = LocationsResponse.newBuilder();

        for (Map<String, Object> row : rows) {
            Location location = Location.newBuilder()
                    .setId((Long) row.get("id"))
                    .setCountry((String) row.get("country"))
                    .setName((String) row.get("name"))
                    .setLatitude((Float)row.get("lat"))
                    .setLongitude((Float)row.get("lon"))
                    .build();
            responseBuilder.addLocations(location);
        }

        LocationsResponse response = responseBuilder.build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public List<Location> executeSearchQuery(String query) {
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Location location = Location.newBuilder()
                    .setId(rs.getLong("id"))
                    .setName(rs.getString("name"))
                    .setCountry(rs.getString("country"))
                    .setLongitude(rs.getFloat("lon"))
                    .setLatitude(rs.getFloat("lat"))
                    .build();

            return location;
        });
    }

    @Override
    public io.grpc.stub.StreamObserver<SearchLocationRequest> search(
            io.grpc.stub.StreamObserver<LocationsResponse> responseObserver) {

        return new StreamObserver<SearchLocationRequest>() {

            @Override
            public void onNext(SearchLocationRequest request) {
                try {

                    SearchLocationQueryBuilder queryBuilder = new SearchLocationQueryBuilder();
                    String query = queryBuilder.build(
                            request.getPatternName(),
                            request.getPatternCountry(),
                            request.getPatternLatitude(),
                            request.getPatternLongitude());


                    List<Location> locations = executeSearchQuery(query);


                    for (Location location : locations) {
                        Location l = Location.newBuilder()
                                .setId(location.getId())
                                .setName(location.getName())
                                .setCountry(location.getCountry())
                                .setLongitude(location.getLongitude())
                                .setLatitude(location.getLatitude())
                                .build();

                        LocationsResponse response = LocationsResponse
                                .newBuilder()
                                .addLocations(l)
                                .build();


                        responseObserver.onNext(response);
                    }


                    responseObserver.onCompleted();

                } catch (Exception e) {

                    responseObserver.onError(e);
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        };
    }
}