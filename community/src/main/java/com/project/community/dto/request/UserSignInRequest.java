package com.project.community.dto.request;

import lombok.Getter;

@Getter
public class UserSignInRequest {
    private String email;
    private String password;
}
