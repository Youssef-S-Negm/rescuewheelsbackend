package com.rescuewheels.backend.enums;

public enum EmergencyRequestState {
    PENDING("PENDING"), RESPONDING("RESPONDING"), IN_PROGRESS("IN_PROGRESS"), DONE("DONE"), CANCELLED("CANCELLED");

    private final String state;

    EmergencyRequestState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
