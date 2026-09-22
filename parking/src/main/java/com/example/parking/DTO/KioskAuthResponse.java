package com.example.parking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KioskAuthResponse {

    private String name;
    private String token;
}