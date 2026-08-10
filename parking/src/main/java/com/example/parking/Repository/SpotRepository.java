package com.example.parking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.VehicleType;
import com.example.parking.model.Spot;

public interface SpotRepository extends JpaRepository<Spot, Integer>{
    Spot findFirstByTypeAndIsAvailableTrue(VehicleType type);
}
