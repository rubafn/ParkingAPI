package com.example.parking.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "{username.required}")
    private String username;

    @NotBlank
    @Size(min = 6, max = 60, message = "{password.size}")
    @Pattern(
        regexp = "^[A-Za-z](?=.*\\d)(?=.*[^A-Za-z\\d]).*$",
        message = "{password.pattern}"
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
