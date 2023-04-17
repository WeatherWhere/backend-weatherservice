package com.weatherwhere.weatherservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class ResultDTO<T> {
    private final int statusCode;
    private final String message;
    private final T data;
}
