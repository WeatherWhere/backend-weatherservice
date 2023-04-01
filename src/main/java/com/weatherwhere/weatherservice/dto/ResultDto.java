package com.weatherwhere.weatherservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class ResultDto<T> {
    private final int resultCode;
    private final String message;
    private final T data;
}
