package com.example.parking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.model.ParkingTicket;

public interface TicketRepository extends JpaRepository<ParkingTicket,Integer>{
    ParkingTicket findByvehicleIDAndexitTimeNull(int id);//this is to check that vehicle doesnt already have an ongoing ticket (double entry) 
}
