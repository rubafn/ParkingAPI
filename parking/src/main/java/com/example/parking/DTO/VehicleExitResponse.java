package com.example.parking.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data 
public class VehicleExitResponse {
    private String plateNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double durationHours;
    private double fee;

    public VehicleExitResponse(String plateNumber, LocalDateTime entryTime, LocalDateTime exitTime, double durationHours,
            double fee) {
        this.plateNumber = plateNumber;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.durationHours = durationHours;
        this.fee = fee;
    }
}
