package com.example.apigateway;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RestLocation {
    private Long id;
    private String name;
    private String country;
    private Double latitude;
    private Double longitude;
}
