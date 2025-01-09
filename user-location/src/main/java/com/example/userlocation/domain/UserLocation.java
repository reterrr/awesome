package com.example.userlocation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "UsersLocations")
public class UserLocation {
    @EmbeddedId
    private UserLocationId userLocationId;
    @Embeddable
    @NoArgsConstructor
    private static class UserLocationId implements Serializable {
        private Long user_id;
        private Long location_id;

        public UserLocationId(Long user_id, Long location_id) {
            this.user_id = user_id;
            this.location_id = location_id;
        }
    }

    public enum SomeEnum {
        SOme,

    }
}
