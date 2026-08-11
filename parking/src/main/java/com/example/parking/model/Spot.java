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
    private int spot_id;
    
    @Enumerated(EnumType.STRING)
    private VehicleType type;

    private boolean is_available;
    private int spot_number;

    @ManyToOne/////////////
    @JoinColumn(name = "branch_id") // FK column /////////////////
    private Branch branch;

    public int getSpotId() {
        return spot_id;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return is_available;
    }

    public void setAvailable(boolean isAvailable) {
        this.is_available = isAvailable;
    }

    public int getSpotNumber() {
        return spot_number;
    }

    public void setSpotNumber(int spotNumber) {
        this.spot_number = spotNumber;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

}
