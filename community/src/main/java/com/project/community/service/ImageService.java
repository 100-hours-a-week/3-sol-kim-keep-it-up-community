package com.project.community.service;

import com.project.community.dto.ImagePostResponseDto;
import com.project.community.dto.ImageResponseDto;
import com.project.community.dto.request.PostImageUploadRequest;
import com.project.community.dto.request.ProfileUploadRequest;
import com.project.community.entity.Image;
import com.project.community.repository.ImageRepository;
import com.project.community.repository.UserRepository;
import com.project.community.util.ImageMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    @Value("${file.path}")
    private String uploadDirectory;

    @PostConstruct
    public void initialize() {
        File directory = new File(uploadDirectory); // 출처: https://sjh9708.tistory.com/94 [데굴데굴 개발자의 기록:티스토리]
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Transactional
    public ImagePostResponseDto uploadProfileImage(ProfileUploadRequest request) {
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html
        MultipartFile file = request.getFile();
        Long userId = request.getUserId();

        String originalFilename = file.getOriginalFilename();
        String filename = LocalDateTime.now() + "_" + originalFilename;
        Image image = new Image(filename, originalFilename, file.getSize(), userId,"profile");

        Path imageFilePath = Paths.get(uploadDirectory, filename);

        try {
            Files.copy(file.getInputStream(), imageFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//        user.setProfileImageUrl("images/" + filename);
//        userRepository.save(user);
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

        String originalFilename = newFile.getOriginalFilename();
        String filename = LocalDateTime.now() + "_" + originalFilename;
        Image image = new Image(filename, originalFilename, newFile.getSize(), userId,"profile");

        Path imageFilePath = Paths.get(uploadDirectory, filename);

        try {
            Files.copy(newFile.getInputStream(), imageFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageRepository.save(image);
        return ImageMapper.toResponseDto(image);
    }

    @Transactional
    public ImagePostResponseDto uploadPostImage(PostImageUploadRequest request) {
        MultipartFile file = request.getFile();
        Long postId = request.getPostId();

        String originalFilename = file.getOriginalFilename();
        String filename = LocalDateTime.now() + "_" + originalFilename;
        Image image = new Image(filename, originalFilename, file.getSize(), postId,"post");

        Path imageFilePath = Paths.get(uploadDirectory, filename);

        try {
            Files.copy(file.getInputStream(), imageFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        String originalFilename = newFile.getOriginalFilename();
        String filename = LocalDateTime.now() + "_" + originalFilename;
        Image image = new Image(filename, originalFilename, newFile.getSize(), postId,"post");

        Path imageFilePath = Paths.get(uploadDirectory, filename);

        try {
            Files.copy(newFile.getInputStream(), imageFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageRepository.save(image);
        return ImageMapper.toResponseDto(image);
    }
}
