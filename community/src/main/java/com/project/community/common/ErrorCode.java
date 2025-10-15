package com.project.community.common;

import com.project.community.util.ErrorMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    EMAIL_CONFLICT(HttpStatus.CONFLICT, ErrorMessage.EMAIL_CONFLICT.getMessage()),
    NICKNAME_CONFLICT(HttpStatus.CONFLICT, ErrorMessage.NICKNAME_CONFLICT.getMessage()),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, ErrorMessage.WRONG_EMAIL.getMessage()),
    WRONG_PASSORD(HttpStatus.UNAUTHORIZED, ErrorMessage.WRONG_PASSWORD.getMessage()),
    USER_GONE(HttpStatus.GONE, ErrorMessage.USER_GONE.getMessage()),
    USER_ALREADY_GONE(HttpStatus.GONE, ErrorMessage.USER_ALREADY_GONE.getMessage());
    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

}
