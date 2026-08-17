package com.example.parking.Exceptions;

public class InvalidVehicleTypeException extends RuntimeException{
    public InvalidVehicleTypeException(String msg){
        super(msg);
    }
}
