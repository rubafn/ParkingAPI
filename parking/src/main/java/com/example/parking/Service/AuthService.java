package com.example.parking.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.parking.UserType;
import com.example.parking.DTO.AuthResponse;
import com.example.parking.DTO.RegisterRequest;
import com.example.parking.Repository.UserRepository;
import com.example.parking.model.Users;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Users user = new Users();

        user.setUsername(request.getUsername());

        user.setPassword(
            passwordEncoder.encode(request.getPassword())
        );

        user.setType(UserType.USER);

        userRepository.save(user);
        return new AuthResponse(user.getUsername(), user.getType());
    }
}
