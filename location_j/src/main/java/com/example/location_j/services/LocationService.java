package com.example.location_j.services;

import com.example.generated.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
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
                    // Build the SQL query
                    SearchLocationQueryBuilder queryBuilder = new SearchLocationQueryBuilder();
                    String query = queryBuilder.build(
                            request.getPatternName(),
                            request.getPatternCountry(),
                            request.getPatternLatitude(),
                            request.getPatternLongitude());

                    // Execute the query and fetch results
                    List<Location> locations = executeSearchQuery(query);

                    // Send the results via responseObserver
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

                    // Indicate the end of the stream
                    responseObserver.onCompleted();

                } catch (Exception e) {
                    // Handle exceptions, e.g., query execution failure
                    responseObserver.onError(e);
                }
            }

            @Override
            public void onError(Throwable t) {
                // Handle any errors that occur during streaming
            }

            @Override
            public void onCompleted() {
                // This is where you can finalize anything after the request is completed
            }
        };
    }
}