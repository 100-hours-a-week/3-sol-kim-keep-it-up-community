package com.project.community.util;

import com.project.community.dto.ImageResponseDto;
import com.project.community.entity.Image;

public class ImageMapper {
    public static ImageResponseDto toResponseDto(Image image) {
        return new ImageResponseDto(image.getId());
    }
}
