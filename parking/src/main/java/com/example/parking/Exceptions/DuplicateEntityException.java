package com.example.parking.Exceptions;

public class DuplicateEntityException extends RuntimeException{
    public DuplicateEntityException(String msg){
        super(msg);
    }
}
