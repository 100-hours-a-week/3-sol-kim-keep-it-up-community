package com.project.community.service;

import com.project.community.dto.UserResponseDto;
import com.project.community.dto.request.UserSignUpRequest;
import com.project.community.repository.RefreshTokenRepository;
import com.project.community.repository.UserRepository;
import com.project.community.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

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
    void signUp() {

        // given
        String nickname = "admin";
        String email = "admin@admin.com";
        String password = "Admin35!";

        String encodedPassword = "encoded-password";

        UserSignUpRequest userSignUpRequest = new UserSignUpRequest(nickname, email,  password);

        Mockito.when(passwordEncoder.encode(password))
                .thenReturn(encodedPassword);

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
                && user.getPassword().equals(encodedPassword)
        ));
        assertThat(result.getNickname()).isEqualTo(nickname);
    }

    @Test
    void getUserInfo() {

    }
}