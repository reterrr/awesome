package com.example.userlocation.repositories;

import com.example.userlocation.domain.UserLocation;
import com.example.userlocation.domain.UserLocationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLocationRepository
        extends JpaRepository<UserLocation, UserLocationId> {
}
