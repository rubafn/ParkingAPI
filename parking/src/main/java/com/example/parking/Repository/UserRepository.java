package com.example.parking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.model.Users;

public interface UserRepository extends JpaRepository<Users,Integer>{
    boolean existsByUsername(String username);
}
