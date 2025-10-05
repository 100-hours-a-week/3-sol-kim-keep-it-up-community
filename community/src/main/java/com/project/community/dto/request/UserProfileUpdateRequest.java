package com.project.community.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfileUpdateRequest {

    private final String nickname;
    // private String profileImageUrl;
}
