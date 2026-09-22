package com.example.parking.DTO;

import com.example.parking.VehicleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data 
public class VehicleEntryRequest {
    @NotBlank(message = "{licencePlate.required}")
    @Pattern( regexp = "[A-Za-z0-9]{2}-[A-Za-z0-9]{3}-[A-Za-z0-9]{2}", message = "{licencePlate.pattern}")
    private String licencePlate;
    @NotNull(message = "{type.required}")
    private VehicleType vehicleType;
    
    public VehicleEntryRequest(){
    }
    public VehicleEntryRequest(String licencePlate, VehicleType vehicleType) {
        this.licencePlate = licencePlate;
        this.vehicleType = vehicleType;
    }
}
