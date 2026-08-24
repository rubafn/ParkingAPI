package com.example.parking.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.VehicleType;
import com.example.parking.model.Spot;

public interface SpotRepository extends JpaRepository<Spot, Integer>{
    Spot findFirstByTypeAndBranchBranchIdAndIsAvailableTrue(VehicleType type,int branchId);
    Page<Spot> findAllByIsAvailableTrue(Pageable pageable);
    Spot findBySpotNumberAndBranchBranchId(int spotNumber, int branchId);
    Page<Spot> findByBranchLocationContainingIgnoreCase(String location, Pageable pageable);
}
