package com.project.community.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostUpdateRequest {

    @NotNull
    private final Long id;
    private final String title;
    private final String contents;

    @NotNull
    private final Long writerId;
}