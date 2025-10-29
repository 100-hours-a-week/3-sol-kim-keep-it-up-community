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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    회원가입
     */
    @Transactional
    public UserResponseDto createUser(UserSignUpRequest userSignUpRequest) {
        String encryptedPassword = passwordEncoder.encode(userSignUpRequest.getPassword());
        String nickname = userSignUpRequest.getNickname();
        String email = userSignUpRequest.getEmail();

        if (userRepository.existsByEmail(email)) throw new CustomException(ErrorCode.EMAIL_CONFLICT);
        if (userRepository.existsByNicknameAndIsDeletedFalse(nickname)) throw new CustomException(ErrorCode.NICKNAME_CONFLICT);

        User user = new User(nickname, userSignUpRequest.getEmail(), encryptedPassword);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    /*
    로그인
     */
    public UserResponseDto signIn(UserSignInRequest userSignInRequest, HttpServletRequest request) {
        String email = userSignInRequest.getEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) throw new CustomException(ErrorCode.WRONG_EMAIL);
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        if (!passwordEncoder.matches(userSignInRequest.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSORD);
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(1800);

        return UserMapper.toResponseDto(user);
    }

    /*
    회원정보 조회(V1)
     */
    public UserProfileResponseDto getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        return UserMapper.toProfileResponseDto(user);
    }

    /*
    회원정보 조회(V2)
     */
    public UserProfileResponseDto getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) throw new CustomException(ErrorCode.SIGNIN_NEEDED);
        Long userId = (Long) session.getAttribute("userId");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        return UserMapper.toProfileResponseDto(user);
    }

    /*
    회원정보 수정(V1)
     */
    @Transactional
    public UserResponseDto updateProfile(Long id, UserProfileUpdateRequest userProfileUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        String nickname = userProfileUpdateRequest.getNickname();

        User userDuplicated = userRepository.findByNicknameAndIsDeletedFalse(nickname);
        if (userDuplicated != null && userDuplicated.getId().equals(user.getId()))
            throw new CustomException(ErrorCode.NICKNAME_CONFLICT);

        user.setNickname(nickname);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    /*
    회원정보 수정(V2)
     */
    @Transactional
    public UserResponseDto updateProfile(HttpServletRequest request, UserProfileUpdateRequest userProfileUpdateRequest) {
        HttpSession session = request.getSession(false);

        if (session == null) throw new CustomException(ErrorCode.SIGNIN_NEEDED);
        Long userId = (Long) session.getAttribute("userId");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        String nickname = userProfileUpdateRequest.getNickname();

        User userDuplicated = userRepository.findByNicknameAndIsDeletedFalse(nickname);
        if (userDuplicated != null && userDuplicated.getId().equals(user.getId()))
            throw new CustomException(ErrorCode.NICKNAME_CONFLICT);

        user.setNickname(nickname);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    /*
    비밀번호 변경(V1)
     */
    @Transactional
    public UserResponseDto updatePassword(Long id, UserPasswordUpdateRequest userPasswordUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        String password = userPasswordUpdateRequest.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    /*
    비밀번호 변경(V2)
     */
    @Transactional
    public UserResponseDto updatePassword(HttpServletRequest request, UserPasswordUpdateRequest userPasswordUpdateRequest) {
        HttpSession session = request.getSession(false);

        if (session == null) throw new CustomException(ErrorCode.SIGNIN_NEEDED);
        Long userId = (Long) session.getAttribute("userId");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        String password = userPasswordUpdateRequest.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        session.invalidate();

        return UserMapper.toResponseDto(user);
    }

    /*
    회원탈퇴(V1)
     */
    @Transactional
    public UserResponseDto withdraw(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_ALREADY_GONE);
        user.setDeleted(true);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    /*
    회원탈퇴(V2)
     */
    @Transactional
    public UserResponseDto withdraw(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) throw new CustomException(ErrorCode.SIGNIN_NEEDED);
        Long userId = (Long) session.getAttribute("userId");

        session.invalidate();

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_ALREADY_GONE);
        user.setDeleted(true);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    /*
    로그아웃
     */
    public void signOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        } else {
            throw new CustomException(ErrorCode.SIGNIN_NEEDED);
        }
    }
}
