package com.project.community.common;

import com.project.community.util.ErrorMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    EMAIL_CONFLICT(HttpStatus.CONFLICT, ErrorMessage.EMAIL_CONFLICT.getMessage()),
    NICKNAME_CONFLICT(HttpStatus.CONFLICT, ErrorMessage.NICKNAME_CONFLICT.getMessage()),
    WRONG_EMAIL(HttpStatus.UNAUTHORIZED, ErrorMessage.WRONG_EMAIL.getMessage()),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, ErrorMessage.USER_NOT_FOUND.getMessage()),
    WRONG_PASSORD(HttpStatus.UNAUTHORIZED, ErrorMessage.WRONG_PASSWORD.getMessage()),
    USER_GONE(HttpStatus.GONE, ErrorMessage.USER_GONE.getMessage()),
    USER_ALREADY_GONE(HttpStatus.GONE, ErrorMessage.USER_ALREADY_GONE.getMessage()),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorMessage.POST_NOT_FOUND.getMessage()),
    POST_DELETED(HttpStatus.GONE, ErrorMessage.POST_GONE.getMessage()),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorMessage.COMMENT_NOT_FOUND.getMessage()),
    COMMENT_ALREADY_DELETED(HttpStatus.GONE, ErrorMessage.COMMENT_ALREADY_GONE.getMessage()),
    ALREADY_LIKED(HttpStatus.CONFLICT, ErrorMessage.ALREADY_LIKED.getMessage()),
    NO_LIKE_TO_CANCEL(HttpStatus.NOT_FOUND, ErrorMessage.LIKE_NOT_FOUND.getMessage()),
    PROFILE_IMAGE_NOT_SET(HttpStatus.NOT_FOUND, ErrorMessage.PROFILE_IMAGE_NOT_SET.getMessage());
    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

}
