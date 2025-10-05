package com.project.community.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSignUpRequest{

    @NotNull
    @NotBlank
    private final String nickname;

    @NotNull
    @NotBlank
    @Email(message = "이메일 형식이 아닙니다.")
    private final String email;

    @NotNull
    @NotBlank
    private final String password;

    // private String final profileImageUrl;
}
