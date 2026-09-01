package com.example.parking.DTO;

import com.example.parking.VehicleType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class SpotUpdateRequest {
    @NotBlank(message = "{type.required}")
    private VehicleType type;
    private Boolean isAvailable;
    @Min(value = 1, message = "{spotNumber.min}")
    @Max(value = 999, message = "{spotNumber.max}")
    private int spotNumber;

    public VehicleType getType() {
        return type;
    }
    public void setType(VehicleType type) {
        this.type = type;
    }
    public Boolean getAvailable() {
        return isAvailable;
    }
    public void setAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    public int getSpotNumber() {
        return spotNumber;
    }
    public void setSpotNumber(int spotNumber) {
        this.spotNumber = spotNumber;
    }
}
