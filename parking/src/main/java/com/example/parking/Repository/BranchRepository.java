package com.example.parking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Integer>{
}
