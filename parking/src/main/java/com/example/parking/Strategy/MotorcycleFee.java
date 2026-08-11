package com.example.parking.Strategy;
import org.springframework.stereotype.Component;

@Component
public class MotorcycleFee implements FeeStrategy{
     @Override
    public double calculateFee(long minutes) {
        return 5*(minutes/60.0);
    }
}
