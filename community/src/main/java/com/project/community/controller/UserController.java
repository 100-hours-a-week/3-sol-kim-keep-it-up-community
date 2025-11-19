package com.project.community.controller;


import com.project.community.common.CustomException;
import com.project.community.common.ErrorCode;
import com.project.community.dto.UserProfileResponseDto;
import com.project.community.dto.UserResponseDto;
import com.project.community.dto.request.UserPasswordUpdateRequest;
import com.project.community.dto.request.UserProfileUpdateRequest;
import com.project.community.dto.request.UserSignInRequest;
import com.project.community.dto.request.UserSignUpRequest;
import com.project.community.dto.response.UserResponse;
import com.project.community.service.UserService;
import com.project.community.common.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
    POST, 회원가입
    => id, 닉네임
     */
    @PostMapping("/signUp")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserSignUpRequest userSignUpRequest) {
        UserResponseDto userResponseDto = userService.createUser(userSignUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.from(Message.SIGNUP_SUCCESS.getMessage(),userResponseDto));
    }

    /*
    POST, 로그인
    => id, 닉네임
     */
    @PostMapping("/signIn")
    public ResponseEntity<UserResponse> signIn(@RequestBody UserSignInRequest userSignInRequest,
                                               HttpServletResponse response) {
        UserResponseDto userResponseDto = userService.signIn(userSignInRequest, response);
        return ResponseEntity.ok(UserResponse.from(Message.SIGNIN_SUCCESS.getMessage(),userResponseDto));
    }

    /*
    GET, 프로필 정보 조회  v1
    => 닉네임, 이메일
     */
    @GetMapping("/v1/{id}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable Long id) {
        UserProfileResponseDto userProfileResponseDto = userService.getUserInfo(id);
        return ResponseEntity.ok(UserResponse.from(Message.USER_INFO_FETCHED.getMessage(), userProfileResponseDto));
    }

    /*
    GET, 프로필 정보 조회 v2
    => 닉네임, 이메일
     */
    @GetMapping
    public ResponseEntity<UserResponse> getUserInfo(HttpServletRequest request) {
        UserProfileResponseDto userProfileResponseDto = userService.getUserInfo(request);
        return ResponseEntity.ok(UserResponse.from(Message.USER_INFO_FETCHED.getMessage(), userProfileResponseDto));
    }

    /*
    PATCH, 프로필 정보 수정 v1
    => id, 닉네임
     */
    @PatchMapping("/v1/{id}")
    public ResponseEntity<UserResponse> updateUserInfo(@PathVariable Long id, @RequestBody UserProfileUpdateRequest userProfileUpdateRequest) {
        UserResponseDto userResponseDto = userService.updateProfile(id, userProfileUpdateRequest);
        return ResponseEntity.ok(UserResponse.from(Message.PROFILE_UPDATE_SUCCESS.getMessage(), userResponseDto));
    }

    /*
    PATCH, 프로필 정보 수정 v2
    => id, 닉네임
     */
    @PatchMapping
    public ResponseEntity<UserResponse> updateUserInfo(HttpServletRequest request, @RequestBody UserProfileUpdateRequest userProfileUpdateRequest) {
        UserResponseDto userResponseDto = userService.updateProfile(request, userProfileUpdateRequest);
        return ResponseEntity.ok(UserResponse.from(Message.PROFILE_UPDATE_SUCCESS.getMessage(), userResponseDto));
    }

    /*
    PATCH, 비밀번호 변경 v1
    => id, 닉네임
     */
    @PatchMapping("/v1/{id}/password")
    public ResponseEntity<UserResponse> updatePassword(@PathVariable Long id, @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {
        UserResponseDto userResponseDto = userService.updatePassword(id, userPasswordUpdateRequest);
        return ResponseEntity.ok(UserResponse.from(Message.PASSWORD_UPDATE_SUCCESS.getMessage(), userResponseDto));
    }

    /*
    PATCH, 비밀번호 변경 v2
    => id, 닉네임
     */
    @PatchMapping("/password")
    public ResponseEntity<UserResponse> updatePassword(HttpServletRequest request, @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {
        UserResponseDto userResponseDto = userService.updatePassword(request, userPasswordUpdateRequest);
        return ResponseEntity.ok(UserResponse.from(Message.PASSWORD_UPDATE_SUCCESS.getMessage(), userResponseDto));
    }

    /*
    DELETE, 회원탈퇴 v1
    => id, 닉네임
     */
    @DeleteMapping("/v1/{id}")
    public ResponseEntity<UserResponse> withdraw(@PathVariable Long id) {
        UserResponseDto userResponseDto = userService.withdraw(id);
        return ResponseEntity.ok(UserResponse.from(Message.WITHDRAWAL_SUCCESS.getMessage(), userResponseDto));
    }

    /*
   DELETE, 회원탈퇴 v3
   => id, 닉네임
    */
    @DeleteMapping
    public ResponseEntity<UserResponse> withdraw(HttpServletRequest request, HttpServletResponse response) {
        UserResponseDto userResponseDto = userService.withdraw(request, response);
        return ResponseEntity.ok(UserResponse.from(Message.WITHDRAWAL_SUCCESS.getMessage(), userResponseDto));
    }

    /*
    DELETE 로그아웃
     */
    @DeleteMapping("/signOut")
    public ResponseEntity<UserResponse> signOut(HttpServletResponse response) {
        userService.signOut(response);
        return ResponseEntity.ok(UserResponse.from(Message.SIGNOUT_SUCCESS.getMessage()));
    }

    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<UserResponse> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                       HttpServletResponse response) {
        if (refreshToken == null) {
            throw new CustomException(ErrorCode.SIGNIN_NEEDED);
        }

        try {
            userService.refreshTokens(refreshToken, response);
            return ResponseEntity.ok(UserResponse.from(Message.TOKEN_REFRESHED.getMessage()));
        } catch (ResponseStatusException exception) {
            throw new CustomException(ErrorCode.SIGNIN_NEEDED);
        }
    }
}
