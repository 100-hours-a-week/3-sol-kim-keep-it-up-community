package com.project.community.dto;


import com.project.community.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PostResponseDto {
    private final Long id;
    private final String title;
    private final String contents;
    private final UserResponseDto writer;
    private final LocalDateTime createdAt;
}
