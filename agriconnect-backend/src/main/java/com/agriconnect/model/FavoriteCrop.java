package com.agriconnect.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "favorite_crops",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_farmer_crop", columnNames = {"farmer_id", "crop_id"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteCrop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "crop_id", nullable = false)
    private Crop crop;
}
