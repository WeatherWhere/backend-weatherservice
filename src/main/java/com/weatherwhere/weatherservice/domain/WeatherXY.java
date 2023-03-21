package com.weatherwhere.weatherservice.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="weather_location", schema = "weather",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"weather_x", "weather_y"})})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Data
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






}
