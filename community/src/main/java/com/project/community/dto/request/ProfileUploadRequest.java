package com.project.community.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfileUploadRequest {
    @NotNull
    private MultipartFile file;

    @NotNull
    private Long userId;
}
