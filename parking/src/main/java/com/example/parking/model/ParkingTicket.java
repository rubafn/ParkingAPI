package com.example.parking.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ParkingTicket {
    
    @Id
    @GeneratedValue
    @Column(name = "ticketID")
    private int ticketID;

    @ManyToOne
    @JoinColumn(name ="vehicleID")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name ="spotID")
    private Spot spot;

    private double fee;
    @Column(name = "entryTime")
    private LocalDateTime entryTime;
    @Column(name = "exitTime")
    private LocalDateTime exitTime;
    
    public int getTicketID() {
        return ticketID;
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    public Spot getSpot() {
        return spot;
    }
    public void setSpot(Spot spot) {
        this.spot = spot;
    }
    public double getFee() {
        return fee;
    }
    public void setFee(double fee) {
        this.fee = fee;
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

    

}
