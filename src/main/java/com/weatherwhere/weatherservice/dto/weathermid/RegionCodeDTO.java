package com.weatherwhere.weatherservice.dto.weathermid;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
/**
 * csv 파일로부터 지역 이름, 지역 코드, 지역 이름을 읽은 데이터를 DTO로 생성
 */
public class RegionCodeDTO {
    private String regionName;
    private String regionCode;
    private String city;
}
