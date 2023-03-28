package com.weatherwhere.weatherservice.domain.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="weather_location", schema = "weather",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"weather_x", "weather_y"})})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class WeatherXY {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //격자 x
    @Column(name = "weather_x")
    private Integer weatherX;

    //격자 y
    @Column(name = "weather_y")
    private Integer weatherY;


    @OneToMany(mappedBy = "weatherXY")
    private List<WeatherShortMain> weatherShortMainList;

    @OneToMany(mappedBy = "weatherXY")
    private List<WeatherShortSub> weatherShortSubList;


}
