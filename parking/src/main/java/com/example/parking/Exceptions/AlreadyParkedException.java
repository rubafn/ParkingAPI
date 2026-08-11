package com.example.parking.Exceptions;

public class AlreadyParkedException extends RuntimeException{
    public AlreadyParkedException(String msg){
        super(msg);
    }
}
