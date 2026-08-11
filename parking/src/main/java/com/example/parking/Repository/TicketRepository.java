package com.example.parking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.model.ParkingTicket;

public interface TicketRepository extends JpaRepository<ParkingTicket,Integer>{
    ParkingTicket findByvehicleIDAndexitTimeNull(int id);//this is to check that vehicle doesnt already have an ongoing ticket (double entry) 

    List<ParkingTicket> findAllByExitTimeNull();//this is to find all ongoing tickets (cars currently parking)
}
