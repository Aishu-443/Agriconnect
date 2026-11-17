package com.agriconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmerProfileDTO {

    private String name;
    private String email;
    private String mobile;
    private String state;
    private String district;
    private String village;
    private BigDecimal farmSize;
    private String primaryCrop;
    private String role;
}
