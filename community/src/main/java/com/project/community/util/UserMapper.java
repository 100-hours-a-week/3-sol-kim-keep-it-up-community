package com.project.community.util;

import com.project.community.dto.UserResponseDto;
import com.project.community.entity.User;

public class UserMapper {
    public static UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getNickname());
    }
}
