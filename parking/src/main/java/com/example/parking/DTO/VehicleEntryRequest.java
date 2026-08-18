package com.example.parking.DTO;

import com.example.parking.VehicleType;

import jakarta.validation.constraints.Pattern;

public class VehicleEntryRequest {
    @Pattern(regexp = "\\d{2}-\\d{3}-\\d{2}", message = "Licence plate must follow the format XX-XXX-XX")
    private String licencePlate;
    private VehicleType vehicleType;
    private int branchId;
    
    public VehicleEntryRequest(){
    }
    public VehicleEntryRequest(String licencePlate, VehicleType vehicleType, int branchId) {
        this.licencePlate = licencePlate;
        this.vehicleType = vehicleType;
        this.branchId= branchId;
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
    public int getBranchId() {
        return branchId;
    }
    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }


    
}
