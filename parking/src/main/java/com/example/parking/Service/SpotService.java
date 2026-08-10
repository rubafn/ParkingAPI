package com.example.parking.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.parking.Repository.SpotRepository;
import com.example.parking.model.Spot;

@Service
public class SpotService {
    private final SpotRepository repository;

    public SpotService(SpotRepository repo){
        this.repository=repo;
    }
    //Read (GET)
    public List<Spot> getAllSpots(String sortBy){
        return this.repository.findAll(Sort.by(sortBy));
    }
    public Optional<Spot> getSpot(int id){
        return this.repository.findById(id);
    }
    

}
