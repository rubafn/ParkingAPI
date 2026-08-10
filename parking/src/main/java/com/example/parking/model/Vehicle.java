package com.example.parking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)////////////
public class Vehicle {
    @Id
    @GeneratedValue
    private String licencePlate;

    public String getLicencePlate() {
        return licencePlate;
    }
}
