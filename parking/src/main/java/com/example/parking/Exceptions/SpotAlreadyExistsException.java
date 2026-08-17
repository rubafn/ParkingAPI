package com.example.parking.Exceptions;

public class SpotAlreadyExistsException extends RuntimeException{
    public SpotAlreadyExistsException(String msg){
        super(msg);
    }
}
