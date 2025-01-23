package com.example.location_j.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Location {
    private Long id;
    private String name;
    private String country;
    private Double latitude;
    private Double longitude;
}
