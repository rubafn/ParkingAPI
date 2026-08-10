package com.example.parking.DTO;

import com.example.parking.VehicleType;

public class VehicleEntryRequest {
    private String licancePlate;
    private VehicleType vehicleType;
    
    public String getLicancePlate() {
        return licancePlate;
    }
    public void setLicancePlate(String licancePlate) {
        this.licancePlate = licancePlate;
    }
    public VehicleType getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    
}
