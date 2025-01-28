package com.rescuewheels.backend.enums;

public enum VehicleType {
    CAR("CAR"), MOTORCYCLE("MOTORCYCLE");

    private final String type;

    VehicleType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
