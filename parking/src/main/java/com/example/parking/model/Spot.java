package com.example.parking.model;

import com.example.parking.VehicleType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data 
@Entity 
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int spotId;
    
    @Enumerated(EnumType.STRING)
    private VehicleType type;

    private boolean isAvailable;
    private int spotNumber;

    @ManyToOne
    @JoinColumn(name = "branch_id") // FK column 
    private Branch branch;
}
