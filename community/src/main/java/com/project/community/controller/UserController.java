package com.project.community.controller;


import com.project.community.dto.UserProfileResponseDto;
import com.project.community.dto.UserResponseDto;
import com.project.community.dto.request.UserProfileUpdateRequest;
import com.project.community.dto.request.UserSignUpRequest;
import com.project.community.dto.response.UserResponse;
import com.project.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable Long id) {
        UserProfileResponseDto userProfileResponseDto = userService.getUserInfo(id);
        return ResponseEntity.ok(UserResponse.from("user info fetched", userProfileResponseDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserInfo(@PathVariable Long id, @RequestBody UserProfileUpdateRequest userProfileUpdateRequest) {
        UserResponseDto userResponseDto = userService.updateProfile(id, userProfileUpdateRequest);
        return ResponseEntity.ok(UserResponse.from("profile updated", userResponseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> withdraw(@PathVariable Long id) {
        UserResponseDto userResponseDto = userService.withdraw(id);
        return ResponseEntity.ok(UserResponse.from("withdrawal success", userResponseDto));
    }
}
