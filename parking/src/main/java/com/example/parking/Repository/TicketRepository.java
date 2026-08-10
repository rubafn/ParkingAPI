package com.example.parking.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.VehicleType;
import com.example.parking.model.ParkingTicket;
import com.example.parking.model.Vehicle;

public interface TicketRepository extends JpaRepository<ParkingTicket,Integer>{
    Optional<Vehicle> findBylicencePlateAndexitTimeNull(String licencePlate);//this is to check that vehicle doesnt already have an ongoing ticket (double entry) 
}
