package com.example.location_j.services;

public class SearchLocationQueryBuilder {

    public String build(String name, String country, double lat, double lon) {
        StringBuilder query = new StringBuilder("SELECT * FROM search_cities(");

        boolean first = true;

        if (!name.isEmpty()) {
            query.append("search_name := '").append(name).append("'");
            first = false;
        }

        if (!country.isEmpty()) {
            if (!first) query.append(",");
            query.append("search_country := '").append(country).append("'");
            first = false;
        }

        if (lat != 0.0) {
            if (!first) query.append(",");
            query.append("search_lat := ").append(lat);
            first = false;
        }

        if (lon != 0.0) {
            if (!first) query.append(",");
            query.append("search_lon := ").append(lon);
        }

        query.append(")");

        return query.toString();
    }
}
