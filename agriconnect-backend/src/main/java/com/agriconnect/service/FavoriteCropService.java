package com.agriconnect.service;

import com.agriconnect.dto.CropDTO;
import com.agriconnect.model.Crop;
import com.agriconnect.model.Farmer;
import com.agriconnect.model.FavoriteCrop;
import com.agriconnect.repository.CropRepository;
import com.agriconnect.repository.FarmerRepository;
import com.agriconnect.repository.FavoriteCropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteCropService {

    private final FavoriteCropRepository favoriteCropRepository;
    private final FarmerRepository farmerRepository;
    private final CropRepository cropRepository;
    private final CropService cropService;

    @Transactional
    public void addFavorite(Long farmerId, Long cropId) {
        if (favoriteCropRepository.existsByFarmer_IdAndCrop_Id(farmerId, cropId)) {
            return; // Already in favorites (idempotent)
        }

        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found with id: " + farmerId));

        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Crop not found with id: " + cropId));

        FavoriteCrop fav = FavoriteCrop.builder()
                .farmer(farmer)
                .crop(crop)
                .build();

        favoriteCropRepository.save(fav);
    }

    @Transactional
    public void removeFavorite(Long farmerId, Long cropId) {
        favoriteCropRepository.deleteByFarmer_IdAndCrop_Id(farmerId, cropId);
    }

    public List<CropDTO> getFavorites(Long farmerId) {
        return favoriteCropRepository.findByFarmer_Id(farmerId)
                .stream()
                .map(FavoriteCrop::getCrop)
                .map(cropService::toDTO)
                .toList();
    }
}
