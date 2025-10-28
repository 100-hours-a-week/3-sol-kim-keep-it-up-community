package com.project.community.service;

import com.project.community.entity.Image;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class FileService {
    @Value("${file.path}")
    private String uploadDirectory;

    @PostConstruct
    public void initialize() {
        File directory = new File(uploadDirectory); // 출처: https://sjh9708.tistory.com/94 [데굴데굴 개발자의 기록:티스토리]
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public Image uploadImage(MultipartFile file, Long id, String type) {
        String originalFilename = file.getOriginalFilename();
        String filename = LocalDateTime.now() + "_" + originalFilename;
        Image image = new Image(filename, originalFilename, file.getSize(), id,type);

        Path imageFilePath = Paths.get(uploadDirectory, filename);

        try {
            Files.copy(file.getInputStream(), imageFilePath);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
