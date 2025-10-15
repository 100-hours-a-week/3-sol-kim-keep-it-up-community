package com.project.community.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException customException) {
        ErrorCode errorCode = customException.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.from(errorCode));
    }
}
