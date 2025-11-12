package com.project.community.service;

import com.project.community.common.CustomException;
import com.project.community.common.ErrorCode;
import com.project.community.dto.TokenResponseDto;
import com.project.community.dto.UserProfileResponseDto;
import com.project.community.dto.UserResponseDto;
import com.project.community.dto.request.UserPasswordUpdateRequest;
import com.project.community.dto.request.UserProfileUpdateRequest;
import com.project.community.dto.request.UserSignInRequest;
import com.project.community.dto.request.UserSignUpRequest;
import com.project.community.entity.RefreshToken;
import com.project.community.entity.User;
import com.project.community.repository.RefreshTokenRepository;
import com.project.community.repository.UserRepository;
import com.project.community.util.JwtUtil;
import com.project.community.util.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

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
    로그인 v3
     */
    @Transactional
    public UserResponseDto signIn(UserSignInRequest userSignInRequest, HttpServletResponse response) {
        String email = userSignInRequest.getEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) throw new CustomException(ErrorCode.WRONG_EMAIL);
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        if (!passwordEncoder.matches(userSignInRequest.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        refreshTokenRepository.deleteByUserId(user.getId());
        TokenResponseDto token = jwtUtil.generateAndSaveTokens(user);
        jwtUtil.addTokenCookies(response, token, null);

        return UserMapper.toResponseDto(user);
    }

    /*
    회원정보 조회 v1
     */
    public UserProfileResponseDto getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        return UserMapper.toProfileResponseDto(user);
    }

    /*
    회원정보 조회 v2
     */
    public UserProfileResponseDto getUserInfoV2(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("userId");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        return UserMapper.toProfileResponseDto(user);
    }

    /*
    회원정보 조회 v3
     */
    public UserProfileResponseDto getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        return UserMapper.toProfileResponseDto(user);
    }

    /*
    회원정보 수정 v1
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
    회원정보 수정 v2
     */
    @Transactional
    public UserResponseDto updateProfileV2(HttpServletRequest request, UserProfileUpdateRequest userProfileUpdateRequest) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("userId");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        String nickname = userProfileUpdateRequest.getNickname();

        User userDuplicated = userRepository.findByNicknameAndIsDeletedFalse(nickname);
        if (userDuplicated != null && !userDuplicated.getId().equals(user.getId()))
            throw new CustomException(ErrorCode.NICKNAME_CONFLICT);

        user.setNickname(nickname);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    /*
   회원정보 수정 v3
    */
    @Transactional
    public UserResponseDto updateProfile(HttpServletRequest request, UserProfileUpdateRequest userProfileUpdateRequest) {
        Long userId = (Long) request.getAttribute("userId");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        String nickname = userProfileUpdateRequest.getNickname();

        User userDuplicated = userRepository.findByNicknameAndIsDeletedFalse(nickname);
        if (userDuplicated != null && !userDuplicated.getId().equals(user.getId()))
            throw new CustomException(ErrorCode.NICKNAME_CONFLICT);

        user.setNickname(nickname);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    /*
    비밀번호 변경 v1
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
    비밀번호 변경 v2
     */
    @Transactional
    public UserResponseDto updatePasswordV2(HttpServletRequest request, UserPasswordUpdateRequest userPasswordUpdateRequest) {
        HttpSession session = request.getSession(false);
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
    비밀번호 변경 v3
     */
    @Transactional
    public UserResponseDto updatePassword(HttpServletRequest request, UserPasswordUpdateRequest userPasswordUpdateRequest) {
        Long userId = (Long) request.getAttribute("userId");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_GONE);
        String password = userPasswordUpdateRequest.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return UserMapper.toResponseDto(user);
    }

    /*
    회원탈퇴 v1
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
    회원탈퇴 v2
     */
    @Transactional
    public UserResponseDto withdrawV2(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("userId");

        session.invalidate();

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_ALREADY_GONE);
        user.setDeleted(true);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }


    /*
   회원탈퇴 v3
    */
    @Transactional
    public UserResponseDto withdraw(HttpServletRequest request, HttpServletResponse response) {
        Long userId = (Long) request.getAttribute("userId");

        TokenResponseDto token = new TokenResponseDto(null, null);
        jwtUtil.addTokenCookies(response, token, 0);

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_ALREADY_GONE);
        user.setDeleted(true);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    /*
    로그아웃
     */
    public void signOut(HttpServletResponse response) {
        TokenResponseDto token = new TokenResponseDto(null, null);
        jwtUtil.addTokenCookies(response, token, 0); // maxAge를 0으로 설정해 즉시 만료시킴.
    }

    /*
   어세스 토큰 재발급
    */
    @Transactional
    public TokenResponseDto refreshTokens(String refreshToken, HttpServletResponse response) {
        var parsedRefreshToken = jwtUtil.parse(refreshToken);

        RefreshToken entity = refreshTokenRepository.findByTokenAndRevokedFalse(refreshToken).orElse(null);

        // 폐기되었거나 만료된 경우
        if (entity == null || entity.getExpiresAt().isBefore(Instant.now())) {
            return null;
        }

        // 유효한 토큰
        Long userId = Long.valueOf(parsedRefreshToken.getBody().getSubject());
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        // 어세스 토큰 발급
        String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail());
        jwtUtil.addTokenCookie(response, "accessToken", newAccessToken, null);

        return new TokenResponseDto(newAccessToken, refreshToken);
    }
}
