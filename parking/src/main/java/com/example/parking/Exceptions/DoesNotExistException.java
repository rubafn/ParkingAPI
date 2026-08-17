package com.example.parking.Exceptions;

public class DoesNotExistException  extends RuntimeException{
    public DoesNotExistException(String msg){
        super(msg);
    }
}
