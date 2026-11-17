package com.agriconnect.controller;

import com.agriconnect.dto.CropDTO;
import com.agriconnect.dto.FarmerProfileDTO;
import com.agriconnect.model.Farmer;
import com.agriconnect.repository.FarmerRepository;
import com.agriconnect.service.FarmerService;
import com.agriconnect.service.FavoriteCropService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;
    private final FavoriteCropService favoriteCropService;
    private final FarmerRepository farmerRepository;

    private Farmer getAuthenticatedFarmer(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        String email = authentication.getName();
        return farmerRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found with email: " + email));
    }

    @GetMapping("/profile")
    public ResponseEntity<FarmerProfileDTO> getProfile(Authentication authentication) {
        FarmerProfileDTO profile = farmerService.getProfile(authentication.getName());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<FarmerProfileDTO> updateProfile(
            Authentication authentication,
            @RequestBody FarmerProfileDTO dto
    ) {
        FarmerProfileDTO updated = farmerService.updateProfile(authentication.getName(), dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/favorites")
    public ResponseEntity<Map<String, String>> addFavorite(
            Authentication authentication,
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam(required = false) Long cropId
    ) {
        Farmer farmer = getAuthenticatedFarmer(authentication);

        Long resolvedCropId = cropId;
        if (resolvedCropId == null && body != null && body.containsKey("cropId")) {
            resolvedCropId = Long.valueOf(body.get("cropId").toString());
        }

        if (resolvedCropId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cropId must be specified in request body or as query parameter");
        }

        favoriteCropService.addFavorite(farmer.getId(), resolvedCropId);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Crop added to favorites"));
    }

    @DeleteMapping("/favorites/{cropId}")
    public ResponseEntity<Map<String, String>> removeFavorite(
            Authentication authentication,
            @PathVariable Long cropId
    ) {
        Farmer farmer = getAuthenticatedFarmer(authentication);
        favoriteCropService.removeFavorite(farmer.getId(), cropId);
        return ResponseEntity.ok(Map.of("message", "Crop removed from favorites"));
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<CropDTO>> getFavorites(Authentication authentication) {
        Farmer farmer = getAuthenticatedFarmer(authentication);
        List<CropDTO> favorites = favoriteCropService.getFavorites(farmer.getId());
        return ResponseEntity.ok(favorites);
    }
}
