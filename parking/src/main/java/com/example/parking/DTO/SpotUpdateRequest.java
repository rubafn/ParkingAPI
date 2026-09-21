package com.example.parking.DTO;

import com.example.parking.VehicleType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpotUpdateRequest {
    @NotNull(message = "{type.required}")
    private VehicleType type;
    private Boolean isAvailable;
    @Min(value = 1, message = "{spotNumber.min}")
    @Max(value = 999, message = "{spotNumber.max}")
    private int spotNumber;
}
