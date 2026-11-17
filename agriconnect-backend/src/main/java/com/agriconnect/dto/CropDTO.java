package com.agriconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropDTO {

    private Long id;
    private String cropName;
    private String category;
    private String season;
    private String soilType;
    private String waterRequirement;
    private String temperatureRange;
    private String cultivationMethod;
    private String harvestingPeriod;
    private String description;
}
