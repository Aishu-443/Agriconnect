package com.agriconnect.repository;

import com.agriconnect.model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {
    List<Crop> findByCategory(String category);
    List<Crop> findByCropNameContainingIgnoreCase(String name);
    List<Crop> findByCropNameContainingIgnoreCaseAndCategory(String name, String category);
}
