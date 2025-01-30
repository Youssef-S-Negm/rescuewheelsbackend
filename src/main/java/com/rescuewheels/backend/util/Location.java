package com.rescuewheels.backend.util;

import com.rescuewheels.backend.entity.common.Coordinate;

public class Location {

    private static final double EARTH_RADIUS = 6371.0; // Earth radius in KMs

    // Calculate the distance between two coordinates using the Haversine formula
    public static double calculateDistance(Coordinate coordinate1, Coordinate coordinate2) {
        double deltaLatitude = Math.toRadians(coordinate2.getLatitude() - coordinate1.getLatitude());
        double deltaLongitude = Math.toRadians(coordinate2.getLongitude() - coordinate1.getLongitude());
        double lat1Rad = Math.toRadians(coordinate1.getLatitude());
        double lat2Rad = Math.toRadians(coordinate2.getLatitude());

        double a = Math.pow(Math.sin(deltaLatitude / 2), 2) +
                Math.pow(Math.sin(deltaLongitude / 2), 2) * Math.cos(lat1Rad) * Math.cos(lat2Rad);

        double c = 2 * Math.asin(Math.sqrt(a));

        return EARTH_RADIUS * c;
    }
}
