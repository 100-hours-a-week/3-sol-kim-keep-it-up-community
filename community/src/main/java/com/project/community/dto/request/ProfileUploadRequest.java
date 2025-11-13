package com.project.community.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUploadRequest {
    @NotNull
    private String imageUrl;

    @NotNull
    private Long userId;
}
