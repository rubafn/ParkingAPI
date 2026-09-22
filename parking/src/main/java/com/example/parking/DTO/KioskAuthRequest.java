package com.example.parking.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KioskAuthRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String secret;
}
