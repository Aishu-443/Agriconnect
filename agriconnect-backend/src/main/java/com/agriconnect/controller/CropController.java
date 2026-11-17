package com.agriconnect.controller;

import com.agriconnect.dto.CropDTO;
import com.agriconnect.service.CropService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crops")
@RequiredArgsConstructor
public class CropController {

    private final CropService cropService;

    @GetMapping
    public ResponseEntity<List<CropDTO>> getAllCrops(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category
    ) {
        List<CropDTO> crops = cropService.getAllCrops(search, category);
        return ResponseEntity.ok(crops);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CropDTO> getCropById(@PathVariable Long id) {
        CropDTO crop = cropService.getCropById(id);
        return ResponseEntity.ok(crop);
    }

    @PostMapping
    public ResponseEntity<CropDTO> addCrop(@RequestBody CropDTO dto) {
        CropDTO created = cropService.addCrop(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CropDTO> updateCrop(@PathVariable Long id, @RequestBody CropDTO dto) {
        CropDTO updated = cropService.updateCrop(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCrop(@PathVariable Long id) {
        cropService.deleteCrop(id);
        return ResponseEntity.ok(Map.of("message", "Crop deleted successfully", "id", id.toString()));
    }
}
