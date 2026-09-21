package com.example.parking.DTO;

import com.example.parking.UserType;

import lombok.Data;

@Data
public class AuthResponse {
    private String username;
    private UserType type;
    private String token;
    
    public AuthResponse(String username, UserType type, String token) {
        this.username = username;
        this.type = type;
        this.token = token;
    }
    public AuthResponse(String username, UserType type) {
        this.username = username;
        this.type = type;
    }
    
} 
