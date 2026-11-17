package com.agriconnect.service;

import com.agriconnect.config.JwtUtil;
import com.agriconnect.dto.LoginRequest;
import com.agriconnect.dto.LoginResponse;
import com.agriconnect.dto.RegisterRequest;
import com.agriconnect.model.Farmer;
import com.agriconnect.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final FarmerRepository farmerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Map<String, String> register(RegisterRequest req) {
        if (farmerRepository.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already registered: " + req.getEmail());
        }

        Farmer farmer = Farmer.builder()
                .name(req.getName())
                .email(req.getEmail())
                .mobile(req.getMobile())
                .password(passwordEncoder.encode(req.getPassword()))
                .state(req.getState())
                .district(req.getDistrict())
                .village(req.getVillage())
                .farmSize(req.getFarmSize())
                .primaryCrop(req.getPrimaryCrop())
                .role("FARMER")
                .build();

        farmerRepository.save(farmer);

        return Map.of(
                "message", "Farmer registered successfully",
                "email", farmer.getEmail()
        );
    }

    public LoginResponse login(LoginRequest req) {
        Farmer farmer = farmerRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(req.getPassword(), farmer.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        String token = jwtUtil.generateToken(farmer.getEmail(), farmer.getRole(), farmer.getId());

        return LoginResponse.builder()
                .token(token)
                .role(farmer.getRole())
                .name(farmer.getName())
                .farmerId(farmer.getId())
                .build();
    }
}
