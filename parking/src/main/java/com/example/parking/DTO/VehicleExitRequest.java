package com.example.parking.DTO;

public class VehicleExitRequest {
    private int branchId;


    public VehicleExitRequest() {
    }

    public VehicleExitRequest(int branchId) {
        this.branchId = branchId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    
}
