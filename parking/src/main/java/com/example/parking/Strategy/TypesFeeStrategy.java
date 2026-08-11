package com.example.parking.Strategy;

import com.example.parking.VehicleType;

public class TypesFeeStrategy {
    public FeeStrategy getFeeStrategy(VehicleType type){
        return switch(type){
            case CAR -> new CarFee();
            case MOTORCYCLE -> new MotorcycleFee();
            case TRUCK -> new TruckFee();
        };

    }
}
