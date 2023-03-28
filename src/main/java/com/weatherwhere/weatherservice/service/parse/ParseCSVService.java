package com.weatherwhere.weatherservice.service.parse;

import com.weatherwhere.weatherservice.dto.parse.RegionCodeDTO;

import java.util.List;

public interface ParseCSVService {
    List<RegionCodeDTO> ParseCSV();
}
