package com.example.parking.Strategy;
import org.springframework.stereotype.Component;

@Component
public class TruckFee implements FeeStrategy{
     @Override
    public double calculateFee(long minutes) {
        return 10*(minutes/60.0);
    }
}
