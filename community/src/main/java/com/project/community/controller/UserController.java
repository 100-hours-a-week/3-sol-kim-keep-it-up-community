package com.project.community.controller;


import com.project.community.dto.UserProfileResponseDto;
import com.project.community.dto.UserResponseDto;
import com.project.community.dto.request.UserPasswordUpdateRequest;
import com.project.community.dto.request.UserProfileUpdateRequest;
import com.project.community.dto.request.UserSignInRequest;
import com.project.community.dto.request.UserSignUpRequest;
import com.project.community.dto.response.UserResponse;
import com.project.community.service.UserService;
import com.project.community.common.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
    POST, 회원가입
    => id, 닉네임
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserSignUpRequest userSignUpRequest) {
        UserResponseDto userResponseDto = userService.createUser(userSignUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.from(Message.SIGNUP_SUCCESS.getMessage(),userResponseDto));
    }

    /*
    POST, 로그인
    => id, 닉네임
     */
    @PostMapping("/signin")
    public ResponseEntity<UserResponse> signIn(@RequestBody UserSignInRequest userSignInRequest) {
        UserResponseDto userResponseDto = userService.signIn(userSignInRequest);
        return ResponseEntity.ok(UserResponse.from(Message.SIGNIN_SUCCESS.getMessage(),userResponseDto));
    }

    /*
    GET, 프로필 정보 조회
    => 닉네임, 이메일
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable Long id) {
        UserProfileResponseDto userProfileResponseDto = userService.getUserInfo(id);
        return ResponseEntity.ok(UserResponse.from(Message.USER_INFO_FETCHED.getMessage(), userProfileResponseDto));
    }

    /*
    PATCH, 프로필 정보 수정
    => id, 닉네임
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserInfo(@PathVariable Long id, @RequestBody UserProfileUpdateRequest userProfileUpdateRequest) {
        UserResponseDto userResponseDto = userService.updateProfile(id, userProfileUpdateRequest);
        return ResponseEntity.ok(UserResponse.from(Message.PROFILE_UPDATE_SUCCESS.getMessage(), userResponseDto));
    }

    /*
    PATCH, 비밀번호 변경
    => id, 닉네임
     */
    @PatchMapping("/{id}/password")
    public ResponseEntity<UserResponse> updatePassword(@PathVariable Long id, @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {
        UserResponseDto userResponseDto = userService.updatePassword(id, userPasswordUpdateRequest);
        return ResponseEntity.ok(UserResponse.from(Message.PASSWORD_UPDATE_SUCCESS.getMessage(), userResponseDto));
    }

    /*
    DELETE, 회원탈퇴
    => id, 닉네임
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> withdraw(@PathVariable Long id) {
        UserResponseDto userResponseDto = userService.withdraw(id);
        return ResponseEntity.ok(UserResponse.from(Message.WITHDRAWAL_SUCCESS.getMessage(), userResponseDto));
    }
}
