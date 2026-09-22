package com.example.parking.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.parking.DTO.KioskAuthRequest;
import com.example.parking.DTO.KioskAuthResponse;
import com.example.parking.Service.KioskService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/kiosk")
public class KioskController {
     private final KioskService kioskService;

    public KioskController(KioskService kioskService) {
        this.kioskService = kioskService;
    }

    @PostMapping("/auth")
    public KioskAuthResponse authenticate(
            @Valid @RequestBody KioskAuthRequest request) {

        return kioskService.authenticate(request);
    }
}
