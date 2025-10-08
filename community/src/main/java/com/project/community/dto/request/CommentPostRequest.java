package com.project.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentPostRequest {

    @NotNull
    private final Long writerId;

    @NotNull
    @NotBlank
    private final String contents;

    @NotNull
    private final Long postId;
}
