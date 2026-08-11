package com.example.parking.model;

import com.example.parking.VehicleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue
    @Column(name = "vehicleID")
    private int vehicleID;

    @Column(name = "licencePlate")
    private String licencePlate;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    public int getVehicleID() {
        return vehicleID;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }


}
