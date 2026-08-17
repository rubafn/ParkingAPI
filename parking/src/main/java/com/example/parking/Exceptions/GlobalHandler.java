package com.example.parking.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(NoVehicleFoundException.class)
    public ResponseEntity<String> handleVehicleNotFound(NoVehicleFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(NoTicketFoundException.class)
    public ResponseEntity<String> handleTicketNotFound(NoTicketFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(NoAvailableSpotsException.class)
    public ResponseEntity<String> handleNoAvailableSpots(NoAvailableSpotsException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(AlreadyParkedException.class)
    public ResponseEntity<String> handleAlreadyParked(AlreadyParkedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(SpotAlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExistingSpot(SpotAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(DoesNotExistException.class)
    public ResponseEntity<String> handleNonExistent(DoesNotExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(InvalidVehicleTypeException.class)
    public ResponseEntity<String> handleInvalidType(InvalidVehicleTypeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
