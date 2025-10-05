package com.project.community.dto.response;

import lombok.Getter;

@Getter
public class UserResponse<T> {

    private String message;

    private T data;

    public UserResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
    public static <T> UserResponse<T> from(String message, T data) {
        return new UserResponse<>(message, data);
    }
}
