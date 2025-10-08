package com.project.community.dto.response;

import lombok.Getter;

@Getter
public class CommentResponse<T> {
    private final String message;
    private final T data;

    public CommentResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> CommentResponse<T> from(String message, T data) {
        return new CommentResponse<>(message, data);
    }
}
