package com.weatherwhere.weatherservice.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RankLocationNXYDTO {
    private String level1;
    private String level2;

    //격자 x
    private Integer weatherX;

    //격자 y
    private Integer weatherY;
}
