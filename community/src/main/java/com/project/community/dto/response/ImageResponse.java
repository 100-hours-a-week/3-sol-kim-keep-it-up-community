package com.project.community.dto.response;

import lombok.Getter;

@Getter
public class ImageResponse<T> {

    private final String message;
    private T data;

    public ImageResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ImageResponse(String message) {
        this.message = message;
    }

    public static <T> ImageResponse<T> from(String message, T data) {
        return new ImageResponse<>(message, data);
    }
}