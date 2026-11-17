package com.agriconnect.service;

import com.agriconnect.dto.MarketPriceDTO;
import com.agriconnect.model.Crop;
import com.agriconnect.model.MarketPrice;
import com.agriconnect.repository.CropRepository;
import com.agriconnect.repository.MarketPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketPriceService {

    private final MarketPriceRepository marketPriceRepository;
    private final CropRepository cropRepository;

    public List<MarketPriceDTO> getAllPrices() {
        return marketPriceRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<MarketPriceDTO> searchPrices(String crop, String state, String district) {
        String cleanCrop = (crop != null && !crop.trim().isEmpty()) ? crop.trim() : null;
        String cleanState = (state != null && !state.trim().isEmpty()) ? state.trim() : null;
        String cleanDistrict = (district != null && !district.trim().isEmpty()) ? district.trim() : null;

        return marketPriceRepository.searchPrices(cleanCrop, cleanState, cleanDistrict)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public MarketPriceDTO addPrice(MarketPriceDTO dto) {
        Crop crop = null;
        if (dto.getCropId() != null) {
            crop = cropRepository.findById(dto.getCropId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Crop not found with id: " + dto.getCropId()));
        } else if (dto.getCropName() != null && !dto.getCropName().trim().isEmpty()) {
            List<Crop> crops = cropRepository.findByCropNameContainingIgnoreCase(dto.getCropName().trim());
            if (!crops.isEmpty()) {
                crop = crops.get(0);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Crop not found with name: " + dto.getCropName());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cropId or cropName is required");
        }

        MarketPrice price = MarketPrice.builder()
                .crop(crop)
                .marketName(dto.getMarketName())
                .district(dto.getDistrict())
                .state(dto.getState())
                .minPrice(dto.getMinPrice())
                .maxPrice(dto.getMaxPrice())
                .modalPrice(dto.getModalPrice())
                .priceDate(dto.getPriceDate() != null ? dto.getPriceDate() : LocalDate.now())
                .source(dto.getSource())
                .build();

        MarketPrice saved = marketPriceRepository.save(price);
        return toDTO(saved);
    }

    public MarketPriceDTO updatePrice(Long id, MarketPriceDTO dto) {
        MarketPrice price = marketPriceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Market price record not found with id: " + id));

        if (dto.getCropId() != null) {
            Crop crop = cropRepository.findById(dto.getCropId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Crop not found with id: " + dto.getCropId()));
            price.setCrop(crop);
        } else if (dto.getCropName() != null && !dto.getCropName().trim().isEmpty()) {
            List<Crop> crops = cropRepository.findByCropNameContainingIgnoreCase(dto.getCropName().trim());
            if (!crops.isEmpty()) {
                price.setCrop(crops.get(0));
            }
        }

        if (dto.getMarketName() != null) price.setMarketName(dto.getMarketName());
        if (dto.getDistrict() != null) price.setDistrict(dto.getDistrict());
        if (dto.getState() != null) price.setState(dto.getState());
        if (dto.getMinPrice() != null) price.setMinPrice(dto.getMinPrice());
        if (dto.getMaxPrice() != null) price.setMaxPrice(dto.getMaxPrice());
        if (dto.getModalPrice() != null) price.setModalPrice(dto.getModalPrice());
        if (dto.getPriceDate() != null) price.setPriceDate(dto.getPriceDate());
        if (dto.getSource() != null) price.setSource(dto.getSource());

        MarketPrice updated = marketPriceRepository.save(price);
        return toDTO(updated);
    }

    public void deletePrice(Long id) {
        if (!marketPriceRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Market price record not found with id: " + id);
        }
        marketPriceRepository.deleteById(id);
    }

    public MarketPriceDTO toDTO(MarketPrice price) {
        if (price == null) return null;
        return MarketPriceDTO.builder()
                .id(price.getId())
                .cropId(price.getCrop() != null ? price.getCrop().getId() : null)
                .cropName(price.getCrop() != null ? price.getCrop().getCropName() : null)
                .marketName(price.getMarketName())
                .district(price.getDistrict())
                .state(price.getState())
                .minPrice(price.getMinPrice())
                .maxPrice(price.getMaxPrice())
                .modalPrice(price.getModalPrice())
                .priceDate(price.getPriceDate())
                .source(price.getSource())
                .build();
    }
}
