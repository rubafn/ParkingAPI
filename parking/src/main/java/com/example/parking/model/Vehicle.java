package com.example.parking.model;

import com.example.parking.VehicleType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vehicleId;

    @Pattern(regexp = "[A-Za-z0-9]{2}-[A-Za-z0-9]{3}-[A-Za-z0-9]{2}", message = "Licence plate must follow the format XX-XXX-XX")
    private String licencePlate;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    public int getVehicleId() {
        return vehicleId;
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
