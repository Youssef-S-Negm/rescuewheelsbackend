package com.rescuewheels.backend.dto;

import com.rescuewheels.backend.entity.User;

public class LoginResponse {

    private User user;
    private String token;

    public LoginResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "user=" + user +
                ", token='" + token + '\'' +
                '}';
    }
}
