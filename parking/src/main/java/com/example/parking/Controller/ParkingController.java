package com.example.parking.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.parking.VehicleType;
import com.example.parking.DTO.SpotAddRequest;
import com.example.parking.DTO.SpotUpdateRequest;
import com.example.parking.DTO.VehicleEntryRequest;
import com.example.parking.DTO.VehicleEntryResponse;
import com.example.parking.DTO.VehicleExitRequest;
import com.example.parking.DTO.VehicleExitResponse;
import com.example.parking.Service.ParkingService;
import com.example.parking.model.Branch;
import com.example.parking.model.ParkingTicket;
import com.example.parking.model.Spot;
import com.example.parking.model.Vehicle;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/parking")
@SecurityRequirement(name = "bearerAuth")
public class ParkingController {
    private final ParkingService service;

    public ParkingController(ParkingService service){
        this.service = service;
    }
    
    @PostMapping("/entry")
    public VehicleEntryResponse enterVehicle(@Valid @RequestBody VehicleEntryRequest request) {
        return this.service.enterVehicle(request);
    }
    @PostMapping("/exit/{plateNumber}")
    public VehicleExitResponse exitVehicle(@PathVariable String plateNumber,VehicleExitRequest request) {
        return this.service.exitVehicle(plateNumber,request);
    }
    @GetMapping("/spots")
    public Page<Spot> getAllSpots(Pageable pageable) {
        return this.service.getAllSpots(pageable);
    }
    @GetMapping("/spots/available")
    public Page<Spot> getAvailableSpots(Pageable pageable){
        return this.service.getAvailableSpots(pageable);
    }
    @GetMapping("/vehicles")
    public Page<Vehicle> getAllVehicles(Pageable pageable){
        return this.service.findAllVehicles(pageable);
    }
    @GetMapping("/vehicles/search")
    public Page<Vehicle> searchVehicles(@RequestParam String plate,Pageable pageable) {
        return this.service.searchVehicles(plate, pageable);
    }
    @GetMapping("/vehicles/{type}")
    public Page<Vehicle> getAllVehiclesByType(@PathVariable VehicleType type,Pageable pageable){
        return this.service.findAllVehiclesByType(type, pageable);
    }
    @GetMapping("/tickets")
    public Page<ParkingTicket> getAllTickets(Pageable pageable){
        return this.service.findAllTickets(pageable);
    }
    @GetMapping("/tickets/ongoing")
    public Page<ParkingTicket> getAllOngoingTickets(Pageable pageable){
        return this.service.findAllOngoingTickets(pageable);
    }
    @GetMapping("/tickets/search")
    public Page<ParkingTicket> searchTickets(@RequestParam String licencePlate,Pageable pageable) {
        return service.searchTickets(licencePlate, pageable);
    }

    @PostMapping("/spots")
    public Spot addNewSpot(@RequestBody SpotAddRequest request) {
        return this.service.addNewSpot(request);
    }
    @GetMapping("/spots/search")
    public Page<Spot> searchSpots( @RequestParam String location,Pageable pageable) {
        return service.searchSpots(location, pageable);
    }
    @PatchMapping("/spots/{id}")
    public Spot UpdateSpot(@PathVariable int id, @RequestBody SpotUpdateRequest request){
        return this.service.UpdateSpot(id, request);
    }
    
    @PostMapping("/branches")
    public Branch addBranch(@RequestBody String location) {
        return this.service.addBranch(location);
    }
    @GetMapping("/branches/search")
    public Page<Branch> searchBranches(@RequestParam String location,Pageable pageable) {
        return service.searchBranches(location, pageable);
    }
    
}
