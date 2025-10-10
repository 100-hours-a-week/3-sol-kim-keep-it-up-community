package com.project.community.dto.request;

import lombok.Getter;

@Getter
public class PostLikeRequest {

    private Long userId;
    private Long postId;
}
