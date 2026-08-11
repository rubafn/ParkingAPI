package com.example.parking.model;

import com.example.parking.VehicleType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Spot {
    @Id
    @GeneratedValue
    private int spotId;
    
    @Enumerated(EnumType.STRING)
    private VehicleType type;

    private boolean isAvailable;
    private int spotNumber;

    @ManyToOne/////////////
    @JoinColumn(name = "branch_id") // FK column /////////////////
    private Branch branch;

    public int getSpotId() {
        return spotId;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(int spotNumber) {
        this.spotNumber = spotNumber;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

}
