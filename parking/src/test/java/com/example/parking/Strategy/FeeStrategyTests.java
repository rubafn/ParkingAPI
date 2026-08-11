package com.example.parking.Strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.parking.VehicleType;
import com.example.parking.Repository.VehicleRepository;

@ExtendWith(MockitoExtension.class)
public class FeeStrategyTests {

    @Mock
    private VehicleRepository vehicleRepository;
    
    @Test
    void CarFeeStrategyTest(){
        
        TypesFeeStrategy typesStrategy = new TypesFeeStrategy();
        FeeStrategy strategy = typesStrategy.getFeeStrategy(VehicleType.CAR);

        assertEquals(70, strategy.calculateFee(10*60));//10 hours
    }
    @Test
    void TruckFeeStrategyTest(){
        TypesFeeStrategy typesStrategy = new TypesFeeStrategy();
        FeeStrategy strategy = typesStrategy.getFeeStrategy(VehicleType.TRUCK);

        assertEquals(70, strategy.calculateFee(7*60));//7 hours
    }
    @Test
    void MotorcycleFeeStrategyTest(){
        
        TypesFeeStrategy typesStrategy = new TypesFeeStrategy();
        FeeStrategy strategy = typesStrategy.getFeeStrategy(VehicleType.MOTORCYCLE);

        assertEquals(25, strategy.calculateFee(5*60));//5 hours
    }
}
