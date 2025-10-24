package com.project.community.service;

import com.project.community.dto.ImagePostResponseDto;
import com.project.community.dto.ImageResponseDto;
import com.project.community.dto.request.ProfileUploadRequest;
import com.project.community.entity.Image;
import com.project.community.repository.ImageRepository;
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
        MultipartFile file = request.getFile();
        Long userId = request.getUserId();

        String originalFilename = file.getOriginalFilename();
//        String path = LocalDateTime.now() + File.separator + filename;
        String filename = LocalDateTime.now() + "_" + originalFilename;
        Image image = new Image(filename, originalFilename, file.getSize(), userId,"profile");

//        Path imageFilePath = Paths.get(uploadDirectory + filename);
        Path imageFilePath = Paths.get(uploadDirectory, filename);

        try {
//            Files.write(imageFilePath, filename.getBytes()); ->  파일 저장 후 파일이 손상되었거나 미리보기가 인식하지 않는 파일 포맷을 사용합니다.
            Files.copy(file.getInputStream(), imageFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageRepository.save(image);
        return ImageMapper.toResponseDto(image);
    }

    public ImageResponseDto getUserProfileImage(Long userId) {
        Image profileImage = imageRepository.findByTypeAndUserId("profile", userId);
        String filename = profileImage.getFilename();
        return ImageMapper.toResponseDto("images/" + filename);
    }
}
