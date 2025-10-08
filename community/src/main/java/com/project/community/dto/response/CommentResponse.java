package com.project.community.dto.response;

import lombok.Getter;

@Getter
public class CommentResponse<T> {
    private final String message;
    private T data;

    public CommentResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public CommentResponse(String message) {
        this.message = message;
    }

    public static <T> CommentResponse<T> from(String message, T data) {
        return new CommentResponse<>(message, data);
    }

    public static <T> CommentResponse<T> from(String message) { return new CommentResponse<>(message);}
}
