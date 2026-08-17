package com.example.parking.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.parking.VehicleType;
import com.example.parking.DTO.SpotAddRequest;
import com.example.parking.DTO.SpotUpdateRequest;
import com.example.parking.DTO.VehicleEntryRequest;
import com.example.parking.DTO.VehicleEntryResponse;
import com.example.parking.DTO.VehicleExitResponse;
import com.example.parking.Service.ParkingService;
import com.example.parking.model.Branch;
import com.example.parking.model.ParkingTicket;
import com.example.parking.model.Spot;
import com.example.parking.model.Vehicle;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;




@RestController
@RequestMapping("/api/parking")
public class ParkingController {
    private final ParkingService service;

    public ParkingController(ParkingService service){
        this.service = service;
    }
    
    @PostMapping("/entry")
    public VehicleEntryResponse enterVehicle(@RequestBody VehicleEntryRequest request) {
        return this.service.enterVehicle(request);
    }
    @PostMapping("/exit/{plateNumber}")
    public VehicleExitResponse exitVehicle(@PathVariable String plateNumber) {
        return this.service.exitVehicle(plateNumber);
    }
    @GetMapping("/spots")
    public List<Spot> getAllSpots() {
        return this.service.getAllSpots();
    }
    @GetMapping("/spots/available")
    public List<Spot> getAvailableSpots(){
        return this.service.getAvailableSpots();
    }
    @GetMapping("/vehicles")
    public List<Vehicle> getAllVehicles(){
        return this.service.findAllVehicles();
    }
    @GetMapping("/vehicles/{type}")
    public List<Vehicle> getAllVehiclesByType(@PathVariable VehicleType type){
        return this.service.findAllVehiclesByType(type);
    }
    @GetMapping("/tickets")
    public List<ParkingTicket> getAllTickets(){
        return this.service.findAllTickets();
    }
    @GetMapping("/tickets/Ongoing")
    public List<ParkingTicket> getAllOngoingTickets(){
        return this.service.findAllOngoingTickets();
    }

    @PostMapping("/spots")
    public Spot addNewSpot(@RequestBody SpotAddRequest request) {
        return this.service.addNewSpot(request);
    }
    @PatchMapping("/spots/{id}")
    public Spot UpdateSpot(@PathVariable int id, @RequestBody SpotUpdateRequest request){
        return this.service.UpdateSpot(id, request);
    }
    
    @PostMapping("/branches")
    public Branch addBranch(@RequestBody String location) {
        return this.service.addBranch(location);
    }
    
}
