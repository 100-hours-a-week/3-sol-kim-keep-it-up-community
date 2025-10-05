package com.project.community.dto;

public class UserResponseDto {

    private Long id;
    private String nickname;

    public UserResponseDto(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
