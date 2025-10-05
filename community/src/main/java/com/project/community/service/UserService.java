package com.project.community.service;

import com.project.community.dto.UserResponseDto;
import com.project.community.dto.request.UserSignUpRequest;
import com.project.community.entity.User;
import com.project.community.repository.UserRepository;
import com.project.community.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserResponseDto createUser(UserSignUpRequest userSignUpRequest) {

        String encryptedPassword = bCryptPasswordEncoder.encode(userSignUpRequest.getPassword());
        User user = new User(userSignUpRequest.getNickname(), userSignUpRequest.getEmail(), encryptedPassword);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }
}
