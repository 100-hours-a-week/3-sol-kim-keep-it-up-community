package com.project.community.dto.response;
import lombok.Getter;

@Getter
public class PostResponse<T> {

    private final String message;
    private T data;

    public PostResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public PostResponse(String message) {
        this.message = message;
    }


    public static <T> PostResponse<T> from(String message, T data) {
        return new PostResponse<>(message, data);
    }

    public static <T> PostResponse<T> from(String message) {
        return new PostResponse<>(message);
    }
}
