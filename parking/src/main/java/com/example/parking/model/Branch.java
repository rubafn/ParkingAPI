package com.example.parking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Branch {
    @Id
    @GeneratedValue
    private int branch_id;

    private String location;
    
    public int getBranchId() {
        return branch_id;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

}
