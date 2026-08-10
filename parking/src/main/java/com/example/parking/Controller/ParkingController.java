package com.example.parking.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.parking.DTO.VehicleEntryRequest;
import com.example.parking.DTO.VehicleEntryResponse;
import com.example.parking.DTO.VehicleExitResponse;
import com.example.parking.Service.ParkingService;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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
    public VehicleExitResponse exitVehicle(@PathVariable String plate) {
        return this.service.exitVehicle(plate);
    }
    
    
}
