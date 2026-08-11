package com.example.parking.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.parking.VehicleType;
import com.example.parking.DTO.VehicleEntryRequest;
import com.example.parking.DTO.VehicleEntryResponse;
import com.example.parking.DTO.VehicleExitResponse;
import com.example.parking.Repository.SpotRepository;
import com.example.parking.Repository.TicketRepository;
import com.example.parking.Repository.VehicleRepository;
import com.example.parking.model.ParkingTicket;
import com.example.parking.model.Spot;
import com.example.parking.model.Vehicle;

@Service
public class ParkingService {
    private final VehicleRepository vehicleRepo;
    private final SpotRepository spotRepo;
    private final TicketRepository ticketRepo;

    public ParkingService(VehicleRepository vehicleRepo, SpotRepository spotRepo, TicketRepository ticketRepo){
        this.spotRepo = spotRepo;
        this.vehicleRepo = vehicleRepo;
        this.ticketRepo = ticketRepo;
    }

    public VehicleEntryResponse enterVehicle(VehicleEntryRequest request){
        String plate = request.getLicencePlate();
        VehicleType type = request.getVehicleType();

        //check for available spots with the suitable type 
        Spot spot = spotRepo.findFirstByTypeAndIsAvailableTrue(type);
        //no available spots
        if(spot==null){
            //throw exception
        }
        //create new vehicle or use existing vehicle
        Vehicle vehicle = vehicleRepo.findByLicencePlate(plate);

        if(vehicle==null){
            vehicle = new Vehicle();
            vehicle.setLicencePlate(plate);
            vehicle.setType(type);
            vehicleRepo.save(vehicle);
        }
        ParkingTicket ticket = ticketRepo.findByVehicleVehicleIDAndExitTimeIsNull(vehicle.getVehicleID());
        if(ticket != null){
            //throw exception that there is already an ongoing ticket
        }
        spot.setAvailable(false);
        spotRepo.save(spot);

        ticket = new ParkingTicket();
        ticket.setSpot(spot);
        ticket.setVehicle(vehicle);
        ticket.setEntryTime(LocalDateTime.now());
        ticketRepo.save(ticket);

        return new VehicleEntryResponse(plate, spot.getSpotNumber(), LocalDateTime.now());
    }

    public VehicleExitResponse exitVehicle(String plate){
        Vehicle vehicle = vehicleRepo.findByLicencePlate(plate);

        if(vehicle == null){//vehicle doesnt have a ticket
            //throw ezxception
        }
        ParkingTicket ticket = ticketRepo.findByVehicleVehicleIDAndExitTimeIsNull(vehicle.getVehicleID());
        if(ticket == null){
            //exception ticket doesnt exist (no entry to exit)
        }
        ticket.setExitTime(LocalDateTime.now());
        Long duration = Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toMinutes();
        double fee = (duration/60.0);
        if(vehicle.getType()==VehicleType.CAR){
            fee *= 7;
        }else if(vehicle.getType()==VehicleType.MOTORCYCLE){
            fee *= 5;
        }else if (vehicle.getType()== VehicleType.TRUCK){
            fee*= 10;
        }
        ticket.setFee(fee);
        ticketRepo.save(ticket);

        Spot spot = ticket.getSpot();
        spot.setAvailable(true);
        spotRepo.save(spot);

        return new VehicleExitResponse(plate, ticket.getEntryTime(), ticket.getExitTime(), duration, fee);
    }
    public List<Spot> getAllSpots(){
        return this.spotRepo.findAll();
    }
    public List<Spot> getAvailableSpots(){
        return this.spotRepo.findAllByIsAvailableTrue();
    }

    public List<Vehicle> findAllVehicles() {
        return this.vehicleRepo.findAll();
    }
    public List<Vehicle> findAllVehiclesByType(VehicleType type){
        return this.vehicleRepo.findAllByType(type);
    }
    public List<ParkingTicket> findAllTickets(){
        return this.ticketRepo.findAll();
    }
    public List<ParkingTicket> findAllOngoingTickets(){
        return this.ticketRepo.findAllByExitTimeIsNull();
    }

}
