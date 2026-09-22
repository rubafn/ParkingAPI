package com.example.parking.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.model.Kiosk;

public interface KioskRepository  extends JpaRepository<Kiosk, Integer>{
    Optional<Kiosk> findByName(String name);
}
