package com.example.parking.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.VehicleType;
import com.example.parking.model.Vehicle;


public interface VehicleRepository extends JpaRepository<Vehicle, Integer>{
    Vehicle findByLicencePlate(String licencePlate);
    Page<Vehicle> findAllByType(VehicleType type, Pageable pageable);
    Page<Vehicle> findByLicencePlateContainingIgnoreCase(String plate,Pageable pageable);
}
