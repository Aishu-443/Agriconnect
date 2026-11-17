package com.agriconnect.repository;

import com.agriconnect.model.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {

    List<MarketPrice> findByCrop_CropNameContainingIgnoreCase(String cropName);

    List<MarketPrice> findByStateIgnoreCase(String state);

    List<MarketPrice> findByCrop_CropNameContainingIgnoreCaseAndStateIgnoreCase(String cropName, String state);

    @Query("SELECT m FROM MarketPrice m WHERE " +
           "(:crop IS NULL OR :crop = '' OR LOWER(m.crop.cropName) LIKE LOWER(CONCAT('%', :crop, '%'))) AND " +
           "(:state IS NULL OR :state = '' OR LOWER(m.state) = LOWER(:state)) AND " +
           "(:district IS NULL OR :district = '' OR LOWER(m.district) = LOWER(:district))")
    List<MarketPrice> searchPrices(
            @Param("crop") String crop,
            @Param("state") String state,
            @Param("district") String district
    );
}
