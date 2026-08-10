package com.example.parking.model;

import java.time.LocalTime;

import org.springframework.data.annotation.Reference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ParkingTicket {
    
    @Id
    @GeneratedValue
    private int ticketID;

    @ManyToOne
    @JoinColumn(name ="vehicleID")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name ="spotID")
    private Spot spot;

    private double fee;
    private LocalTime entryTime;
    private LocalTime exitTime; 

}
