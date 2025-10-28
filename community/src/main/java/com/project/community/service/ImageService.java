package com.project.community.service;

import com.project.community.common.CustomException;
import com.project.community.common.ErrorCode;
import com.project.community.dto.ImagePostResponseDto;
import com.project.community.dto.ImageResponseDto;
import com.project.community.dto.request.PostImageUploadRequest;
import com.project.community.dto.request.ProfileUploadRequest;
import com.project.community.entity.Image;
import com.project.community.entity.User;
import com.project.community.repository.ImageRepository;
import com.project.community.repository.UserRepository;
import com.project.community.util.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Transactional
    public ImagePostResponseDto uploadProfileImage(ProfileUploadRequest request) {
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html
        MultipartFile file = request.getFile();
        Long userId = request.getUserId();

        Image image = fileService.uploadImage(file, userId, "profile");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setProfileImageUrl("images/" + image.getFilename());
        userRepository.save(user);

        imageRepository.save(image);
        return ImageMapper.toResponseDto(image);
    }

    public ImageResponseDto getUserProfileImage(Long userId) {
        Image profileImage = imageRepository.findByTypeAndUserId("profile", userId);
        if (profileImage == null) {
            return null;
        }
        String filename = profileImage.getFilename();
        return ImageMapper.toResponseDto("images/" + filename);
    }

    @Transactional
    public ImagePostResponseDto updateUserProfileImage(ProfileUploadRequest request) {
        Long userId = request.getUserId();
        Image prevImage = imageRepository.findByTypeAndUserId("profile", userId);
        if (prevImage != null) {
            prevImage.setUserId(null);
        }

        MultipartFile newFile = request.getFile();
        Image image = fileService.uploadImage(newFile, userId, "profile");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setProfileImageUrl("images/" + image.getFilename());

        imageRepository.save(image);
        return ImageMapper.toResponseDto(image);
    }

    @Transactional
    public ImagePostResponseDto uploadPostImage(PostImageUploadRequest request) {
        MultipartFile file = request.getFile();
        Long postId = request.getPostId();

        Image image = fileService.uploadImage(file, postId, "post");
//        Post post = userRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//        user.setProfileImageUrl("images/" + image.getFilename());

        imageRepository.save(image);
        return ImageMapper.toResponseDto(image);
    }

    public ImageResponseDto getPostImage(Long postId) {
        Image image = imageRepository.findByTypeAndPostId("post", postId);
        if (image == null) {
            return null;
        }
        String filename = image.getFilename();
        return ImageMapper.toResponseDto("images/" + filename);
    }

    @Transactional
    public ImagePostResponseDto updatePostImage(PostImageUploadRequest request) {
        MultipartFile newFile = request.getFile();
        Long postId = request.getPostId();
        Image prevImage = imageRepository.findByTypeAndPostId("post", postId);
        prevImage.setPostId(null);

        Image image = fileService.uploadImage(newFile, postId, "post");

        imageRepository.save(image);
        return ImageMapper.toResponseDto(image);
    }
}
