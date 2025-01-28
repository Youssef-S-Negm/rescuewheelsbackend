package com.rescuewheels.backend.enums;

public enum UserStatus {
    ONLINE("ONLINE"),
    OFFLINE("OFFLINE");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
