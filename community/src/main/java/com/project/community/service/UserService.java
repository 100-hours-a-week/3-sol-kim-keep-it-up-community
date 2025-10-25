package com.project.community.service;

import com.project.community.common.CustomException;
import com.project.community.common.ErrorCode;
import com.project.community.dto.UserProfileResponseDto;
import com.project.community.dto.UserResponseDto;
import com.project.community.dto.request.UserPasswordUpdateRequest;
import com.project.community.dto.request.UserProfileUpdateRequest;
import com.project.community.dto.request.UserSignInRequest;
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

    @Transactional
    public UserResponseDto createUser(UserSignUpRequest userSignUpRequest) {
        String encryptedPassword = bCryptPasswordEncoder.encode(userSignUpRequest.getPassword());
        String nickname = userSignUpRequest.getNickname();
        String email = userSignUpRequest.getEmail();

        if (userRepository.existsByEmail(email)) throw new CustomException(ErrorCode.EMAIL_CONFLICT);

        if (userRepository.existsByNickname(nickname)) {
            User user = userRepository.findByNickname(nickname);
            if (!user.isDeleted())
                throw new CustomException(ErrorCode.NICKNAME_CONFLICT);
        }

        User user = new User(nickname, userSignUpRequest.getEmail(), encryptedPassword);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    public UserResponseDto signIn(UserSignInRequest userSignInRequest) {
        String email = userSignInRequest.getEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) throw new CustomException(ErrorCode.WRONG_EMAIL);
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        if (!bCryptPasswordEncoder.matches(userSignInRequest.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSORD);
        }
        return UserMapper.toResponseDto(user);
    }

    public UserProfileResponseDto getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        return UserMapper.toProfileResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateProfile(Long id, UserProfileUpdateRequest userProfileUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        String nickname = userProfileUpdateRequest.getNickname();
        if (userRepository.existsByNickname(nickname)) {
            User userNicknameDuplicated = userRepository.findByNickname(nickname);
            if (!userNicknameDuplicated.isDeleted() && !user.getId().equals(userNicknameDuplicated.getId()))
                throw new CustomException(ErrorCode.NICKNAME_CONFLICT);
        }
        user.setNickname(nickname);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto updatePassword(Long id, UserPasswordUpdateRequest userPasswordUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        String password = userPasswordUpdateRequest.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto withdraw(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_ALREADY_GONE);
        user.setDeleted(true);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }
}
