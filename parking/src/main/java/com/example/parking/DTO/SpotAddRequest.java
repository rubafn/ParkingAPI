package com.example.parking.DTO;

import com.example.parking.VehicleType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class SpotAddRequest {
    private int branchId;
    @Min(value = 1, message = "{spotNumber.min}")
    @Max(value = 999, message = "{spotNumber.max}")
    private int spotNumber;
    @NotBlank(message = "{type.required}")
    private VehicleType type;

    
    public int getSpotNumber() {
        return spotNumber;
    }
    public void setSpotNumber(int spotNumber) {
        this.spotNumber = spotNumber;
    }
    public VehicleType getType() {
        return type;
    }
    public void setType(VehicleType type) {
        this.type = type;
    }
    public int getBranchId() {
        return branchId;
    }
    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

}
