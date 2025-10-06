package com.project.community.dto.response;

import com.project.community.dto.PostResponseDto;
import com.project.community.entity.Post;
import lombok.Getter;

@Getter
public class PostResponse<T> {

    private String message;
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
