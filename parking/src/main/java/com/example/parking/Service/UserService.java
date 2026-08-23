package com.example.parking.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.parking.Exceptions.DoesNotExistException;
import com.example.parking.Repository.UserRepository;
import com.example.parking.UserType;
import com.example.parking.DTO.UserResponse;
import com.example.parking.model.Users;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse makeAdmin(String username) {

        Users user = userRepository.findByUsername(username).orElseThrow(() -> new DoesNotExistException("User not found"));

        user.setRole(UserType.ADMIN);

        userRepository.save(user);
        return new UserResponse(user.getUsername(), user.getRole());
    }
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getUsername(),user.getRole()))
                .collect(Collectors.toList());
    }
}