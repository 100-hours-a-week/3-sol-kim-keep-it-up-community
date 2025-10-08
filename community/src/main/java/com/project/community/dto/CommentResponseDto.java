package com.project.community.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentResponseDto {
    private final Long id;
    private final UserResponseDto writer;
    private final Long posdId;
    private final String contents;
    private final LocalDateTime createdAt;
}
