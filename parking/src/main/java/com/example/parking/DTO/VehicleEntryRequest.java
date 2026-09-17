package com.example.parking.DTO;

import com.example.parking.VehicleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class VehicleEntryRequest {
    @NotBlank(message = "{licencePlate.required}")
    @Pattern( regexp = "[A-Za-z0-9]{2}-[A-Za-z0-9]{3}-[A-Za-z0-9]{2}", message = "{licencePlate.pattern}")
    private String licencePlate;
    @NotNull(message = "{type.required}")
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
