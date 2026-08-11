package com.example.parking.DTO;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class VehicleExitResponse {
    private String plateNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private long durationHours;
    private double fee;

    public VehicleExitResponse(String plateNumber, LocalDateTime entryTime, LocalDateTime exitTime, long durationHours,
            double fee) {
        this.plateNumber = plateNumber;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.durationHours = durationHours;
        this.fee = fee;
    }
    public String getPlateNumber() {
        return plateNumber;
    }
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
    public LocalDateTime getExitTime() {
        return exitTime;
    }
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
    public long getDurationHours() {
        return durationHours;
    }
    public void setDurationHours(long durationHours) {
        this.durationHours = durationHours;
    }
    public double getFee() {
        return fee;
    }
    public void setFee(double fee) {
        this.fee = fee;
    }

    
}
