package com.project.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostRequest {
    @NotNull
    @NotBlank
    private final String title;

    @NotNull
    @NotBlank
    private final String contents;

    @NotNull
    private final Long writerId;
}
