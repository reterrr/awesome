package com.example.apigateway.Requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchRequest {
    private String name;
    private String country;
    private double longitude;
    private double latitude;
}
