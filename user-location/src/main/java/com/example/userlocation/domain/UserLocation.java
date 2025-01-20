package com.example.userlocation.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@Builder
@Getter
@Table(name = "UsersLocations")
public class UserLocation {
    @EmbeddedId
    private UserLocationId userLocationId;

    @Builder
    public UserLocation(UserLocationId userLocationId) {
        this.userLocationId = userLocationId;
    }
}