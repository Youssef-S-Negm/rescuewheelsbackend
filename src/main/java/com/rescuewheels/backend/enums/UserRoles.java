package com.rescuewheels.backend.enums;

public enum UserRoles {
    USER("ROLE_USER"),
    TECHNICIAN("ROLE_TECHNICIAN"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    UserRoles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
