package com.agriconnect.repository;

import com.agriconnect.model.FavoriteCrop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FavoriteCropRepository extends JpaRepository<FavoriteCrop, Long> {

    List<FavoriteCrop> findByFarmer_Id(Long farmerId);

    boolean existsByFarmer_IdAndCrop_Id(Long farmerId, Long cropId);

    @Transactional
    @Modifying
    void deleteByFarmer_IdAndCrop_Id(Long farmerId, Long cropId);
}
