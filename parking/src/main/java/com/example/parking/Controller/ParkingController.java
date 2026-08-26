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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public VehicleExitResponse exitVehicle(@PathVariable String plateNumber, @Valid @RequestBody VehicleExitRequest request) {
        return this.service.exitVehicle(plateNumber,request);
    }
    @GetMapping("/spots")
    @PreAuthorize("isAuthenticated()")
    public Page<Spot> getAllSpots( @ParameterObject Pageable pageable) {
        return this.service.getAllSpots(pageable);
    }
    @GetMapping("/spots/available")
    @PreAuthorize("isAuthenticated()")
    public Page<Spot> getAvailableSpots( @ParameterObject Pageable pageable){
        return this.service.getAvailableSpots(pageable);
    }
    @GetMapping("/vehicles")
    @PreAuthorize("isAuthenticated()")
    public Page<Vehicle> getAllVehicles( @ParameterObject Pageable pageable){
        return this.service.findAllVehicles(pageable);
    }
    @GetMapping("/vehicles/search")
    @PreAuthorize("isAuthenticated()")
    public Page<Vehicle> searchVehicles(@RequestParam String plate, @ParameterObject Pageable pageable) {
        return this.service.searchVehicles(plate, pageable);
    }
    @GetMapping("/vehicles/{type}")
    @PreAuthorize("isAuthenticated()")
    public Page<Vehicle> getAllVehiclesByType(@PathVariable VehicleType type, @ParameterObject Pageable pageable){
        return this.service.findAllVehiclesByType(type, pageable);
    }
    @GetMapping("/tickets")
    @PreAuthorize("isAuthenticated()")
    public Page<ParkingTicket> getAllTickets( @ParameterObject Pageable pageable){
        return this.service.findAllTickets(pageable);
    }
    @GetMapping("/tickets/ongoing")
    @PreAuthorize("isAuthenticated()")
    public Page<ParkingTicket> getAllOngoingTickets( @ParameterObject Pageable pageable){
        return this.service.findAllOngoingTickets(pageable);
    }
    @GetMapping("/tickets/search")
    @PreAuthorize("isAuthenticated()")
    public Page<ParkingTicket> searchTickets(@RequestParam String licencePlate, @ParameterObject Pageable pageable) {
        return service.searchTickets(licencePlate, pageable);
    }

    @PostMapping("/spots")
    @PreAuthorize("hasRole('ADMIN')")
    public Spot addNewSpot(@RequestBody SpotAddRequest request) {
        return this.service.addNewSpot(request);
    }
    @GetMapping("/spots/search")
    @PreAuthorize("isAuthenticated()")
    public Page<Spot> searchSpots( @RequestParam String location, @ParameterObject Pageable pageable) {
        return service.searchSpots(location, pageable);
    }
    @PatchMapping("/spots/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Spot UpdateSpot(@PathVariable int id, @RequestBody SpotUpdateRequest request){
        return this.service.UpdateSpot(id, request);
    }
    
    @PostMapping("/branches")
    @PreAuthorize("hasRole('ADMIN')")
    public Branch addBranch(@RequestBody String location) {
        return this.service.addBranch(location);
    }
    @GetMapping("/branches/search")
    @PreAuthorize("isAuthenticated()")
    public Page<Branch> searchBranches(@RequestParam String location, @ParameterObject Pageable pageable) {
        return service.searchBranches(location, pageable);
    }
    
}
