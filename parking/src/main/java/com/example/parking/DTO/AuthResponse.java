package com.example.parking.DTO;

import com.example.parking.UserType;

public class AuthResponse {
    private String username;
    private UserType type;
    
    public AuthResponse(String username, UserType type) {
        this.username = username;
        this.type = type;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public UserType getType() {
        return type;
    }
    public void setType(UserType type) {
        this.type = type;
    }

    
} 
