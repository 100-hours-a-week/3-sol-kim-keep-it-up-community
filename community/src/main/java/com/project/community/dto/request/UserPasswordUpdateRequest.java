package com.project.community.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPasswordUpdateRequest {
    private final String password;
}
