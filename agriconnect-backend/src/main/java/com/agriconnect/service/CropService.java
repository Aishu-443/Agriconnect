package com.agriconnect.service;

import com.agriconnect.dto.CropDTO;
import com.agriconnect.model.Crop;
import com.agriconnect.repository.CropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CropService {

    private final CropRepository cropRepository;

    public List<CropDTO> getAllCrops(String search, String category) {
        List<Crop> crops;

        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasCategory = category != null && !category.trim().isEmpty();

        if (hasSearch && hasCategory) {
            crops = cropRepository.findByCropNameContainingIgnoreCaseAndCategory(search.trim(), category.trim().toUpperCase());
        } else if (hasSearch) {
            crops = cropRepository.findByCropNameContainingIgnoreCase(search.trim());
        } else if (hasCategory) {
            crops = cropRepository.findByCategory(category.trim().toUpperCase());
        } else {
            crops = cropRepository.findAll();
        }

        return crops.stream().map(this::toDTO).toList();
    }

    public CropDTO getCropById(Long id) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Crop not found with id: " + id));
        return toDTO(crop);
    }

    public CropDTO addCrop(CropDTO dto) {
        Crop crop = Crop.builder()
                .cropName(dto.getCropName())
                .category(dto.getCategory() != null ? dto.getCategory().toUpperCase() : null)
                .season(dto.getSeason())
                .soilType(dto.getSoilType())
                .waterRequirement(dto.getWaterRequirement())
                .temperatureRange(dto.getTemperatureRange())
                .cultivationMethod(dto.getCultivationMethod())
                .harvestingPeriod(dto.getHarvestingPeriod())
                .description(dto.getDescription())
                .build();

        Crop saved = cropRepository.save(crop);
        return toDTO(saved);
    }

    public CropDTO updateCrop(Long id, CropDTO dto) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Crop not found with id: " + id));

        if (dto.getCropName() != null) crop.setCropName(dto.getCropName());
        if (dto.getCategory() != null) crop.setCategory(dto.getCategory().toUpperCase());
        if (dto.getSeason() != null) crop.setSeason(dto.getSeason());
        if (dto.getSoilType() != null) crop.setSoilType(dto.getSoilType());
        if (dto.getWaterRequirement() != null) crop.setWaterRequirement(dto.getWaterRequirement());
        if (dto.getTemperatureRange() != null) crop.setTemperatureRange(dto.getTemperatureRange());
        if (dto.getCultivationMethod() != null) crop.setCultivationMethod(dto.getCultivationMethod());
        if (dto.getHarvestingPeriod() != null) crop.setHarvestingPeriod(dto.getHarvestingPeriod());
        if (dto.getDescription() != null) crop.setDescription(dto.getDescription());

        Crop updated = cropRepository.save(crop);
        return toDTO(updated);
    }

    public void deleteCrop(Long id) {
        if (!cropRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Crop not found with id: " + id);
        }
        cropRepository.deleteById(id);
    }

    public CropDTO toDTO(Crop crop) {
        if (crop == null) return null;
        return CropDTO.builder()
                .id(crop.getId())
                .cropName(crop.getCropName())
                .category(crop.getCategory())
                .season(crop.getSeason())
                .soilType(crop.getSoilType())
                .waterRequirement(crop.getWaterRequirement())
                .temperatureRange(crop.getTemperatureRange())
                .cultivationMethod(crop.getCultivationMethod())
                .harvestingPeriod(crop.getHarvestingPeriod())
                .description(crop.getDescription())
                .build();
    }
}
