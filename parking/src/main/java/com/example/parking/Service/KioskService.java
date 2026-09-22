package com.example.parking.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.parking.DTO.KioskAuthRequest;
import com.example.parking.DTO.KioskAuthResponse;
import com.example.parking.Repository.KioskRepository;
import com.example.parking.Security.JwtService;
import com.example.parking.model.Kiosk;

@Service
public class KioskService {

    private final KioskRepository kioskRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public KioskService(
            KioskRepository kioskRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.kioskRepository = kioskRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public KioskAuthResponse authenticate(KioskAuthRequest request) {

        Kiosk kiosk = kioskRepository.findByName(request.getName())
                .orElseThrow(() ->
                        new RuntimeException("Invalid kiosk credentials"));

        if (!kiosk.isEnabled()) {
            throw new RuntimeException("Kiosk is disabled");
        }

        if (!passwordEncoder.matches(
                request.getSecret(),
                kiosk.getSecret())) {

            throw new RuntimeException("Invalid kiosk credentials");
        }

        String token = jwtService.generateKioskToken(kiosk);

        return new KioskAuthResponse(
                kiosk.getName(),
                token
        );
    }
}