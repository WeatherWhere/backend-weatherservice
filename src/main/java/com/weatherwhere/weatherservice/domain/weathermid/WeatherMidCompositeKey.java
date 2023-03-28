package com.weatherwhere.weatherservice.domain.weathermid;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
public class WeatherMidCompositeKey implements Serializable {
    // 예보 구역 코드
    @Column(name = "region_code")
    private String regionCode;
    // 예보 날짜
    @Column(name = "base_date")
    private String baseTime;
}
