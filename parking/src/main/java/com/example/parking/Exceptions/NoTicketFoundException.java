package com.example.parking.Exceptions;

public class NoTicketFoundException extends RuntimeException{
    public NoTicketFoundException(String msg){
        super(msg);
    }
}
