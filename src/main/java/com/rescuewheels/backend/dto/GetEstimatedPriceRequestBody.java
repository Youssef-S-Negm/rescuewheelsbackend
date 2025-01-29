package com.rescuewheels.backend.dto;

import com.rescuewheels.backend.entity.common.Coordinate;

public class GetEstimatedPriceRequestBody {

    private Coordinate startCoordinate;
    private Coordinate endCoordinate;

    public GetEstimatedPriceRequestBody() {
    }

    public GetEstimatedPriceRequestBody(Coordinate startCoordinate, Coordinate endCoordinate) {
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
    }

    public Coordinate getStartCoordinate() {
        return startCoordinate;
    }

    public void setStartCoordinate(Coordinate startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    public Coordinate getEndCoordinate() {
        return endCoordinate;
    }

    public void setEndCoordinate(Coordinate endCoordinate) {
        this.endCoordinate = endCoordinate;
    }

    @Override
    public String toString() {
        return "GetEstimatedPriceRequestBody{" +
                "startCoordinate=" + startCoordinate +
                ", endCoordinate=" + endCoordinate +
                '}';
    }
}
