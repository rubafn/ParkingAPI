package com.example.parking.model;

import com.example.parking.VehicleType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue
    private int vehicle_id;

    private String licence_plate;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    public int getVehicleId() {
        return vehicle_id;
    }

    public String getLicencePlate() {
        return licence_plate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licence_plate = licencePlate;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }


}
