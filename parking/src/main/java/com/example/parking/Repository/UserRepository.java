package com.example.parking.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.model.Users;

public interface UserRepository extends JpaRepository<Users,Integer>{
    boolean existsByUsername(String username);
    Optional<Users> findByUsername(String username);
    Page<Users> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
