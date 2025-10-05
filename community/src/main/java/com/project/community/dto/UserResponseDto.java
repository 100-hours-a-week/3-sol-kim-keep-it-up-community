package com.project.community.dto;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String nickname;

    public UserResponseDto(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
