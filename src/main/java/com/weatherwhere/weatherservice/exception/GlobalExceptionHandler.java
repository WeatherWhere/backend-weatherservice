package com.weatherwhere.weatherservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // 상황에 맞게 최적화 및 커스터마이징

    /*
     * 직접 정의한 RestApiException 을 호출하게 되면 객체를 받아 에러 코드를 생성하고 handleExceptionInternal 메서드를 호출
     */
    @ExceptionHandler(RestApiException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(RestApiException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        String customMessage = ex.getCustomMessage();
        if (customMessage == null || customMessage.length() == 0) {
            return handleExceptionInternal(errorCode);
        }
        return handleExceptionInternal(errorCode, customMessage);
    };

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ErrorResponse> handleNoSuchElementException(Exception e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        return handleExceptionInternal(errorCode);
    }



    // handleExceptionInternal() 메소드를 오버라이딩해 응답 커스터마이징
    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ErrorResponse(errorCode));
    }

    // 추가적으로 message를 던져주고 싶을 경우
    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode, String customMessage) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ErrorResponse(errorCode, customMessage));
    }
}