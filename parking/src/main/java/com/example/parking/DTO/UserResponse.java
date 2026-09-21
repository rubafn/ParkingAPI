package com.example.parking.DTO;

import com.example.parking.UserType;

import lombok.Data;

@Data 
public class UserResponse {
    private String username;
    private UserType role;
    
    public UserResponse(String username, UserType role) {
        this.username = username;
        this.role = role;
    }
}
