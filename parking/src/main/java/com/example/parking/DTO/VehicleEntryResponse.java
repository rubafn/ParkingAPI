package com.example.parking.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data 
public class VehicleEntryResponse {
    private String licencePlate;
    private int branchId;
    private int assignedSpot;
    private LocalDateTime entryTime;

    public VehicleEntryResponse(int branchId, String licencePlate, int assignedSpot, LocalDateTime entry){
        this.branchId=branchId;
        this.licencePlate= licencePlate;
        this.assignedSpot=assignedSpot;
        this.entryTime = entry;
    }
}
