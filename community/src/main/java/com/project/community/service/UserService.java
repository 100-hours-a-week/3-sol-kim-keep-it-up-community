package com.project.community.service;

import com.project.community.dto.UserProfileResponseDto;
import com.project.community.dto.UserResponseDto;
import com.project.community.dto.request.UserProfileUpdateRequest;
import com.project.community.dto.request.UserSignInRequest;
import com.project.community.dto.request.UserSignUpRequest;
import com.project.community.entity.User;
import com.project.community.repository.UserRepository;
import com.project.community.util.ErrorMessage;
import com.project.community.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public UserResponseDto createUser(UserSignUpRequest userSignUpRequest) {

        String encryptedPassword = bCryptPasswordEncoder.encode(userSignUpRequest.getPassword());
        String nickname = userSignUpRequest.getNickname();
        String email = userSignUpRequest.getEmail();

        if (userRepository.existsByEmail(email)) throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.EMAIL_CONFLICT.getMessage());

        if (userRepository.existsByNickname(nickname)) {
            User user = userRepository.findByNickname(nickname);
            if (!user.isDeleted())
                throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.NICKNAME_CONFLICT.getMessage());
        }

        User user = new User(nickname, userSignUpRequest.getEmail(), encryptedPassword);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    public UserResponseDto signIn(UserSignInRequest userSignInRequest) {
        String email = userSignInRequest.getEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.WRONG_EMAIL.getMessage());
        if (!bCryptPasswordEncoder.matches(userSignInRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.WRONG_PASSWORD.getMessage());
        }
        return UserMapper.toResponseDto(user);
    }

    public UserProfileResponseDto getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND.getMessage()));
        if (user.isDeleted()) throw new ResponseStatusException(HttpStatus.GONE, ErrorMessage.USER_GONE.getMessage());
        return UserMapper.toProfileResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateProfile(Long id, UserProfileUpdateRequest userProfileUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND.getMessage()));
        if (user.isDeleted()) throw new ResponseStatusException(HttpStatus.GONE, ErrorMessage.USER_GONE.getMessage());
        String nickname = userProfileUpdateRequest.getNickname();
        if (userRepository.existsByNickname(nickname)) {
            User userNicknameDuplicated = userRepository.findByNickname(nickname);
            if (!userNicknameDuplicated.isDeleted())
                throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.NICKNAME_CONFLICT.getMessage());
        }
        user.setNickname(nickname);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto withdraw(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND.getMessage()));
        if (user.isDeleted()) throw new ResponseStatusException(HttpStatus.GONE, ErrorMessage.USER_ALREADY_GONE.getMessage());
        user.setDeleted(true);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

}
