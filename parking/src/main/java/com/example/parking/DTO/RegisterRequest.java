package com.example.parking.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "username cannot be null")
    private String username;

    @NotBlank
    @Size(min = 6, max = 60, message = "password must be 6 characters or more and cannot exceed 60 characters")
    @Pattern(
        regexp = "^[A-Za-z](?=.*\\d)(?=.*[^A-Za-z\\d]).*$",
        message = "Password must start with a letter and contain at least one number and one special character"
    )
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
