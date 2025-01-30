package com.example.userlocation.repositories;

import com.example.userlocation.domain.UserLocation;
import com.example.userlocation.domain.UserLocationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLocationRepository
        extends JpaRepository<UserLocation, UserLocationId> {
    @Query("SELECT DISTINCT ul.userLocationId.location_id FROM UserLocation ul")
    Iterable<Long> findAllDistinctLocationIds();

    @Query("SELECT ul FROM UserLocation ul WHERE ul.userLocationId.user_id = :userId")
    List<UserLocation> findAllByUserId(Long userId);

    @Query("SELECT ul FROM UserLocation ul WHERE ul.userLocationId.location_id = :locationId")
    List<UserLocation> findAllByLocationId(Long locationId);
}
