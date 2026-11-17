package com.agriconnect.service;

import com.agriconnect.dto.FarmerProfileDTO;
import com.agriconnect.model.Farmer;
import com.agriconnect.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FarmerService {

    private final FarmerRepository farmerRepository;

    public FarmerProfileDTO getProfile(String email) {
        Farmer farmer = farmerRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found with email: " + email));

        return toDTO(farmer);
    }

    public FarmerProfileDTO updateProfile(String email, FarmerProfileDTO dto) {
        Farmer farmer = farmerRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found with email: " + email));

        if (dto.getName() != null) farmer.setName(dto.getName());
        if (dto.getMobile() != null) farmer.setMobile(dto.getMobile());
        if (dto.getState() != null) farmer.setState(dto.getState());
        if (dto.getDistrict() != null) farmer.setDistrict(dto.getDistrict());
        if (dto.getVillage() != null) farmer.setVillage(dto.getVillage());
        if (dto.getFarmSize() != null) farmer.setFarmSize(dto.getFarmSize());
        if (dto.getPrimaryCrop() != null) farmer.setPrimaryCrop(dto.getPrimaryCrop());

        Farmer updated = farmerRepository.save(farmer);
        return toDTO(updated);
    }

    public FarmerProfileDTO toDTO(Farmer farmer) {
        return FarmerProfileDTO.builder()
                .name(farmer.getName())
                .email(farmer.getEmail())
                .mobile(farmer.getMobile())
                .state(farmer.getState())
                .district(farmer.getDistrict())
                .village(farmer.getVillage())
                .farmSize(farmer.getFarmSize())
                .primaryCrop(farmer.getPrimaryCrop())
                .role(farmer.getRole())
                .build();
    }
}
