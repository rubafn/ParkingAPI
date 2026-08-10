package com.example.parking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Spot {
    @Id
    @GeneratedValue
    private int spotID;
    
    private char type;
    private boolean free;

    @ManyToOne/////////////
    @JoinColumn(name = "branchID") // FK column /////////////////
    private Branch branch;

    public int getSpotID() {
        return spotID;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

}
