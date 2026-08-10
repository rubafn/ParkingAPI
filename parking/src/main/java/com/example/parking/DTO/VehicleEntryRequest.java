package com.example.parking.DTO;

import com.example.parking.VehicleType;

public class VehicleEntryRequest {
    private String licencePlate;
    private VehicleType vehicleType;
    
    public VehicleEntryRequest(String licencePlate, VehicleType vehicleType) {
        this.licencePlate = licencePlate;
        this.vehicleType = vehicleType;
    }
    public String getLicencePlate() {
        return licencePlate;
    }
    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }
    public VehicleType getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }


    
}
