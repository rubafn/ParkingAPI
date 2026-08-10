package com.example.parking.DTO;

import java.time.LocalTime;

public class VehicleEntryResponse {
    
    private String licencePlate;
    private int assignedSpot;
    private LocalTime entryTime;

    public VehicleEntryResponse(String licencePlate, int assignedSpot, LocalTime entry){
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

    public LocalTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalTime entryTime) {
        this.entryTime = entryTime;
    }

    
}
