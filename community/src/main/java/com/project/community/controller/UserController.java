package com.project.community.controller;


import com.project.community.dto.UserResponseDto;
import com.project.community.dto.request.UserSignUpRequest;
import com.project.community.dto.response.UserResponse;
import com.project.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserSignUpRequest userSignUpRequest) {
        UserResponseDto userResponseDto = userService.createUser(userSignUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.from("signup success",userResponseDto));
    }

}
