package com.project.community.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter // ResponseEntity의 body에 포함되는 부분으로 Jackson이 빈 Bean으로 인식하지 않고 필드를 인식하게 만들기 위해 붙인다. 안 쓸 시 406 에러 발생.
@RequiredArgsConstructor
public class PostResponseDto {
    private final Long id;
    private final String title;
    private final String contents;
    private final UserResponseDto writer;
    private final LocalDateTime createdAt;
    private final int likesCount;
}
