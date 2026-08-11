package com.example.parking.Strategy;

import org.springframework.stereotype.Component;

@Component
public interface FeeStrategy {
    public double calculateFee(long minutes);
}
