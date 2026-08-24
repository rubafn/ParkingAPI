package com.example.parking.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.model.ParkingTicket;

public interface TicketRepository extends JpaRepository<ParkingTicket,Integer>{
    ParkingTicket findByVehicleVehicleIdAndExitTimeIsNull(int vehicleID);//this is to check that vehicle doesnt already have an ongoing ticket (double entry) 

    Page<ParkingTicket> findAllByExitTimeIsNull(Pageable pageable);//this is to find all ongoing tickets (cars currently parking)
    Page<ParkingTicket> findByVehicleLicencePlateContainingIgnoreCase(String licencePlate,Pageable pageable);
}
