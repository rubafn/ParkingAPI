package com.example.parking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.VehicleType;
import com.example.parking.model.Vehicle;


public interface VehicleRepository extends JpaRepository<Vehicle, Integer>{
    Vehicle findByLicencePlate(String licencePlate);
    List<Vehicle> findAllByType(VehicleType type);
}
