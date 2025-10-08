package com.project.community.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentPostRequest {
    private final Long writerId;
    private final String contents;
    private final Long postId;
}
