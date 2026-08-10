package com.example.parking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer>{
}
