package com.rescuewheels.backend.enums;

public enum EmergencyRequestType {
    FLAT_TIRE("FLAT_TIRE"),
    OUT_OF_FUEL_DEAD_BATTERY("OUT_OF_FUEL_DEAD_BATTERY"),
    OTHER("OTHER");

    private final String stringValue;

    EmergencyRequestType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
