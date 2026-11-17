package com.agriconnect.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "crops")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "crop_name", nullable = false)
    private String cropName;

    @Column(nullable = false)
    private String category; // FOOD, COMMERCIAL, PULSES, OILSEEDS, HORTICULTURAL

    private String season;

    @Column(name = "soil_type")
    private String soilType;

    @Column(name = "water_requirement")
    private String waterRequirement;

    @Column(name = "temperature_range")
    private String temperatureRange;

    @Column(name = "cultivation_method")
    private String cultivationMethod;

    @Column(name = "harvesting_period")
    private String harvestingPeriod;

    @Column(columnDefinition = "TEXT")
    private String description;
}
