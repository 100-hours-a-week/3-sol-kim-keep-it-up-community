package com.project.community.service;

import com.project.community.dto.ImageResponseDto;
import com.project.community.dto.request.ProfileUploadRequest;
import com.project.community.entity.Image;
import com.project.community.repository.ImageRepository;
import com.project.community.util.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Transactional
    public ImageResponseDto uploadProfileImage(ProfileUploadRequest request) {
        MultipartFile file = request.getFile();

        String filename = file.getName();
        String path = LocalDateTime.now() + filename;
        Image image = new Image(path, filename, file.getSize());

        Path imageFilePath = Paths.get(uploadDirectory + filename);

        try {
            Files.write(imageFilePath, filename.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageRepository.save(image);
        return ImageMapper.toResponseDto(image);
    }
}
