package com.weatherwhere.weatherservice.dto.weathermid;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegionCodeDTO {
    private String regionName;
    private String regionCode;
    private String city;
}
