package com.project.community.dto.response;

import lombok.Getter;

@Getter
public class PostLikeResponse<T> {

    private final String message;
    private T data;

    public PostLikeResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public PostLikeResponse(String message) {
        this.message = message;
    }


    public static <T> PostLikeResponse<T> from(String message, T data) {
        return new PostLikeResponse<>(message, data);
    }

    public static <T> PostLikeResponse<T> from(String message) {
        return new PostLikeResponse<>(message);
    }
}
