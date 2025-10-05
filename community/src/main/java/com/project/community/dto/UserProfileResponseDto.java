package com.project.community.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfileResponseDto {
    private final String nickname;
    private final String email;
    // private String profileImageUrl;
}
