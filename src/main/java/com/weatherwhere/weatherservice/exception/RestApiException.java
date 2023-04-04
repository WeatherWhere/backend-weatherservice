package com.weatherwhere.weatherservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class RestApiException extends RuntimeException{
    private ErrorCode errorCode;
    private String customMessage;

    // 일반적인 경우
    public RestApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 추가적인 메시지가 필요할 경우
    public RestApiException(ErrorCode errorCode, String customMessage) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }
}
