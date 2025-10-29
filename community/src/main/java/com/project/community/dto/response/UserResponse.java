package com.project.community.dto.response;

import lombok.Getter;

@Getter
public class UserResponse<T> {

    private final String message;

    private final T data;

    public UserResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public UserResponse(String message) {
        this.message = message;
        this.data = null;
    }
    public static <T> UserResponse<T> from(String message, T data) {
        return new UserResponse<>(message, data);
    }

    public static <T> UserResponse<T> from(String message) {
        return new UserResponse<>(message);
    }
}
