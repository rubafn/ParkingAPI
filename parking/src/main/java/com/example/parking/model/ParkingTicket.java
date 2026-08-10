package com.example.parking.model;

import java.time.LocalTime;

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
    public int getTicketID() {
        return ticketID;
    }
    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
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
    public LocalTime getEntryTime() {
        return entryTime;
    }
    public void setEntryTime(LocalTime entryTime) {
        this.entryTime = entryTime;
    }
    public LocalTime getExitTime() {
        return exitTime;
    }
    public void setExitTime(LocalTime exitTime) {
        this.exitTime = exitTime;
    } 

    

}
