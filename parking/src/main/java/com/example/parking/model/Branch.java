package com.example.parking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Branch {
    @Id
    @GeneratedValue
    private int branchID;
    
    private String location;
    public int getBranchID() {
        return branchID;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

}
