package com.project.community.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String message; // 필요하면 timestamp, path, code 등 확장
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getErrorMessage());
    }
}