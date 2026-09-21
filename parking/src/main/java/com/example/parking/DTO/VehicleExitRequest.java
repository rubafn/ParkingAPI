package com.example.parking.DTO;

import lombok.Data;

@Data 
public class VehicleExitRequest {
    private int branchId;


    public VehicleExitRequest() {
    }

    public VehicleExitRequest(int branchId) {
        this.branchId = branchId;
    }
}
