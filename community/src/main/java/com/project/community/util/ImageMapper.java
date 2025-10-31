package com.project.community.util;

import com.project.community.dto.ImagePostResponseDto;
import com.project.community.dto.ImageResponseDto;
import com.project.community.entity.Image;

public class ImageMapper {
    public static ImagePostResponseDto toResponseDto(Image image) {
        return new ImagePostResponseDto(image.getId());
    }

    public static ImageResponseDto toResponseDto(String fileName) {
        return new ImageResponseDto(fileName);
    }
}
