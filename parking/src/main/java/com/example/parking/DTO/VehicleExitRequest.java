package com.example.parking.DTO;

import jakarta.validation.constraints.NotBlank;

public class VehicleExitRequest {
    @NotBlank(message ="please specify branch Id")
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
