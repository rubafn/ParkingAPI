package com.example.parking.DTO;

import com.example.parking.UserType;

public class UserResponse {
    private String username;
    private UserType role;
    
    public UserResponse(String username, UserType role) {
        this.username = username;
        this.role = role;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public UserType getRole() {
        return role;
    }
    public void setRole(UserType role) {
        this.role = role;
    }

    
}
