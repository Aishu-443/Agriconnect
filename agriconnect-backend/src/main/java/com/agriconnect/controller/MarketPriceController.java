package com.agriconnect.controller;

import com.agriconnect.dto.MarketPriceDTO;
import com.agriconnect.service.MarketPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/market-prices")
@RequiredArgsConstructor
public class MarketPriceController {

    private final MarketPriceService marketPriceService;

    @GetMapping
    public ResponseEntity<List<MarketPriceDTO>> getAllPrices() {
        List<MarketPriceDTO> prices = marketPriceService.getAllPrices();
        return ResponseEntity.ok(prices);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MarketPriceDTO>> searchPrices(
            @RequestParam(required = false) String crop,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String district
    ) {
        List<MarketPriceDTO> prices = marketPriceService.searchPrices(crop, state, district);
        return ResponseEntity.ok(prices);
    }

    @PostMapping
    public ResponseEntity<MarketPriceDTO> addPrice(@RequestBody MarketPriceDTO dto) {
        MarketPriceDTO created = marketPriceService.addPrice(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketPriceDTO> updatePrice(@PathVariable Long id, @RequestBody MarketPriceDTO dto) {
        MarketPriceDTO updated = marketPriceService.updatePrice(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePrice(@PathVariable Long id) {
        marketPriceService.deletePrice(id);
        return ResponseEntity.ok(Map.of("message", "Market price record deleted successfully", "id", id.toString()));
    }
}
