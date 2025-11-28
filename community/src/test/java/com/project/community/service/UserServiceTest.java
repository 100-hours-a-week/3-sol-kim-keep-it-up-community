package com.project.community.service;

import com.project.community.common.CustomException;
import com.project.community.common.ErrorCode;
import com.project.community.dto.UserResponseDto;
import com.project.community.dto.request.UserSignUpRequest;
import com.project.community.repository.RefreshTokenRepository;
import com.project.community.repository.UserRepository;
import com.project.community.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    RefreshTokenRepository refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
    JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);

    UserService userService = new UserService(
            userRepository,
            passwordEncoder,
            refreshTokenRepository,
            jwtUtil
    );

    @Test
    void signUp_success() {

        // given
        String nickname = "admin";
        String email = "admin@admin.com";
        String password = "Admin35!";
        String encryptedPassword = "encrypted-password";
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest(nickname, email,  password);

        Mockito.when(passwordEncoder.encode(password))
                .thenReturn(encryptedPassword);

        Mockito.when(userRepository.existsByEmail(email))
                .thenReturn(false);

        Mockito.when(userRepository.existsByNicknameAndIsDeletedFalse(nickname))
                .thenReturn(false);

        // when
        UserResponseDto result = userService.createUser(userSignUpRequest);

        // then
        Mockito.verify(userRepository).save(Mockito.argThat(user ->
                user.getNickname().equals(nickname)
                && user.getEmail().equals(email)
                && user.getPassword().equals(encryptedPassword)
        ));
        assertThat(result.getNickname()).isEqualTo(nickname);
    }

    @Test
    void signUp_fail_nicknameConflict() {

        // given
        String nickname = "admin";
        String email = "admin@admin.com";
        String password = "Admin35!";
        String encryptedPassword = "encrypted-password";
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest(nickname, email,  password);

        Mockito.when(passwordEncoder.encode(password))
                .thenReturn(encryptedPassword);

        Mockito.when(userRepository.existsByEmail(email))
                .thenReturn(false);

        Mockito.when(userRepository.existsByNicknameAndIsDeletedFalse(nickname))
                .thenReturn(true);


        // when & then
        CustomException e = assertThrows(CustomException.class, () -> userService.createUser(userSignUpRequest));

        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NICKNAME_CONFLICT);
    }
}