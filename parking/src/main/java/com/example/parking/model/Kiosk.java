package com.example.parking.model;

import com.example.parking.KioskType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data 
@Entity 
public class Kiosk {
    @Id 
    @GeneratedValue 
    private int kioskId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String secret;

    @ManyToOne 
    @JoinColumn(name="branch_id")
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KioskType type;

    private boolean enabled = true;

}
