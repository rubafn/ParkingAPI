package com.example.parking.Exceptions;

public class NoVehicleFoundException extends RuntimeException{
    public NoVehicleFoundException(String msg){
        super(msg);
    }
}
