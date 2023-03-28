package com.weatherwhere.weatherservice.service.weathermid;

import com.weatherwhere.weatherservice.dto.weathermid.RegionCodeDTO;

import java.util.List;

public interface ParseCSVService {
    List<RegionCodeDTO> ParseCSV();
}
