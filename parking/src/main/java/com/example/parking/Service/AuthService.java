package com.example.parking.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.parking.UserType;
import com.example.parking.DTO.AuthResponse;
import com.example.parking.DTO.RegisterRequest;
import com.example.parking.Exceptions.DoesNotExistException;
import com.example.parking.Exceptions.DuplicateEntityException;
import com.example.parking.Repository.UserRepository;
import com.example.parking.Security.CustomUserDetailsService;
import com.example.parking.Security.JwtService;
import com.example.parking.model.Users;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.parking.DTO.LoginRequest;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager, CustomUserDetailsService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userService;
    }
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateEntityException("Username already exists");
        }

        Users user = new Users();

        user.setUsername(request.getUsername());

        user.setPassword(
            passwordEncoder.encode(request.getPassword())
        );

        user.setRole(UserType.USER);

        userRepository.save(user);
        return new AuthResponse(user.getUsername(), user.getRole());
    }

   public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        Users user = userRepository.findByUsername(request.getUsername()).orElseThrow(() ->new DoesNotExistException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                user.getUsername(),
                user.getRole(),
                token
        );
    }
}
