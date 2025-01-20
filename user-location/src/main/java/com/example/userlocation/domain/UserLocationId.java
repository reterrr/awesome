package com.example.userlocation.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@Builder
public class UserLocationId implements Serializable {
    private long user_id;
    private long location_id;

    @Builder
    public UserLocationId(long user_id, long location_id) {
        this.user_id = user_id;
        this.location_id = location_id;
    }
}
