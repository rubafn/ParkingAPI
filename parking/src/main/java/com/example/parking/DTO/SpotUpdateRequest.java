package com.example.parking.DTO;

import com.example.parking.VehicleType;

public class SpotUpdateRequest {
    private VehicleType type;
    private Boolean isAvailable;
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
