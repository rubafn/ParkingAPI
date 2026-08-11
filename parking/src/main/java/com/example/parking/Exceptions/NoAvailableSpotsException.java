package com.example.parking.Exceptions;

public class NoAvailableSpotsException extends RuntimeException{
    public NoAvailableSpotsException(String message){
        super(message);
    }
}
