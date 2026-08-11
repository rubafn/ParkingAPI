package com.example.parking.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ParkingTicket {
    
    @Id
    @GeneratedValue
    private int ticket_id;

    @ManyToOne
    @JoinColumn(name ="vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name ="spot_id")
    private Spot spot;

    private double fee;
    private LocalDateTime entry_time;
    private LocalDateTime exit_time;

    public int getTicketId() {
        return ticket_id;
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
        return entry_time;
    }
    public void setEntryTime(LocalDateTime entryTime) {
        this.entry_time = entryTime;
    }
    public LocalDateTime getExitTime() {
        return exit_time;
    }
    public void setExitTime(LocalDateTime exitTime) {
        this.exit_time = exitTime;
    } 

    

}
