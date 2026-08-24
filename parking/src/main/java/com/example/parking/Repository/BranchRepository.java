package com.example.parking.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Integer>{
    Branch findByLocation(String location);
    Page<Branch> findByLocationContainingIgnoreCase(String location,Pageable pageable);
}
