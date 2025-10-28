package com.project.community.util;

import com.project.community.dto.UserProfileResponseDto;
import com.project.community.dto.UserResponseDto;
import com.project.community.entity.User;

public class UserMapper {
    public static UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getNickname(), user.getProfileImageUrl());
    }

    public static UserProfileResponseDto toProfileResponseDto(User user) {
        return new UserProfileResponseDto(user.getNickname(), user.getEmail(), user.getProfileImageUrl());
    }
}
