package com.project.community.service;

import com.project.community.dto.UserProfileResponseDto;
import com.project.community.dto.UserResponseDto;
import com.project.community.dto.request.UserProfileUpdateRequest;
import com.project.community.dto.request.UserSignUpRequest;
import com.project.community.entity.User;
import com.project.community.repository.UserRepository;
import com.project.community.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

        if (userRepository.existsByEmail(email)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email has already been taken");

        if (userRepository.existsByNickname(nickname)) {
            User user = userRepository.findByNickname(nickname);
            if (!user.isDeleted())
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Nickname has already been taken");
        }

        User user = new User(nickname, userSignUpRequest.getEmail(), encryptedPassword);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    public UserProfileResponseDto getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.isDeleted()) throw new ResponseStatusException(HttpStatus.GONE, "User has withdrawn");
        return UserMapper.toProfileResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateProfile(Long id, UserProfileUpdateRequest userProfileUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.isDeleted()) throw new ResponseStatusException(HttpStatus.GONE, "User has withdrawn");
        String nickname = userProfileUpdateRequest.getNickname();
        if (userRepository.existsByNickname(nickname)) {
            User userNicknameDuplicated = userRepository.findByNickname(nickname);
            if (!userNicknameDuplicated.isDeleted())
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Nickname has already been taken");
        }
        user.setNickname(nickname);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto withdraw(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.isDeleted()) throw new ResponseStatusException(HttpStatus.GONE, "User has already withdrawn");
        user.setDeleted(true);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }
}
