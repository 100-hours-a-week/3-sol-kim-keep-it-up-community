package com.project.community.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PostLikeResponseDto {
    private boolean isLiked;
}
