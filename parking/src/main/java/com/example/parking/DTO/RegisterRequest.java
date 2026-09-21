package com.example.parking.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 
public class RegisterRequest {
    @NotBlank(message = "{username.required}")
    private String username;

    @NotBlank(message = "{password.required}")
    @Size(min = 6, max = 60, message = "{password.size}")
    @Pattern(
        regexp = "^[A-Za-z](?=.*\\d)(?=.*[^A-Za-z\\d]).*$",
        message = "{password.pattern}"
    )
    private String password;
}
