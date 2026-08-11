package com.example.parking.DTO;

import java.time.LocalDateTime;

public class VehicleEntryResponse {
    
    private String licencePlate;
    private int assignedSpot;
    private LocalDateTime entryTime;

    public VehicleEntryResponse(String licencePlate, int assignedSpot, LocalDateTime entry){
        this.licencePlate= licencePlate;
        this.assignedSpot=assignedSpot;
        this.entryTime = entry;
    }
    public int getAssignedSpot() {
        return assignedSpot;
    }

    public void setAssignedSpot(int assignedSpot) {
        this.assignedSpot = assignedSpot;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    
}
