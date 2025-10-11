package com.project.community.dto.request;

import lombok.Getter;

@Getter
public class PostLikeRequest {

    private Long userId; // Spring Security 사용으로 인증인가 구현 시 변경할 부분
}
