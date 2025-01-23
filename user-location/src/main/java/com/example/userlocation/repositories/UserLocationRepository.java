package com.example.userlocation.repositories;

import com.example.userlocation.domain.UserLocation;
import com.example.userlocation.domain.UserLocationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLocationRepository
        extends JpaRepository<UserLocation, UserLocationId> {
    @Query("SELECT DISTINCT ul.userLocationId.location_id FROM UserLocation ul")
    Iterable<Long> findAllDistinctLocationIds();
}
