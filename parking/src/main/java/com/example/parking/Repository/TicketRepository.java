package com.example.parking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.model.ParkingTicket;

public interface TicketRepository extends JpaRepository<ParkingTicket,Integer>{
}
