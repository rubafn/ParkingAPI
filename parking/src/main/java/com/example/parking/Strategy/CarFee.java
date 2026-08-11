package com.example.parking.Strategy;

import org.springframework.stereotype.Component;

@Component
public class CarFee implements FeeStrategy{

    @Override
    public double calculateFee(long minutes) {
        return 7*(minutes/60.0);
    }
    
}
