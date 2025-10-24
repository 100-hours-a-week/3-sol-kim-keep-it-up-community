package com.project.community.controller;

import com.project.community.dto.ImagePostResponseDto;
import com.project.community.dto.ImageResponseDto;
import com.project.community.dto.request.ProfileUploadRequest;
import com.project.community.dto.response.ImageResponse;
import com.project.community.service.ImageService;
import com.project.community.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /*

     */
    @PostMapping("/profiles")
    public ResponseEntity<ImageResponse> uploadProfileImage(ProfileUploadRequest request) {
        ImagePostResponseDto imagePostResponseDto = imageService.uploadProfileImage(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ImageResponse.from(Message.PROFILE_IMAGE_POST_SUCCESS.getMessage(), imagePostResponseDto));
    }

    @GetMapping("profiles/{userId}")
    public ResponseEntity<ImageResponse> getUserProfileImage(@PathVariable Long userId) {
        ImageResponseDto imageResponseDto = imageService.getUserProfileImage(userId);
        return ResponseEntity.ok(ImageResponse.from(Message.PROFILE_IMAGE_RETURNED.getMessage(), imageResponseDto));
    }

//    @PutMapping("/profiles")
//    public ResponseEntity<ImageResponse> updateProfileImage() {
//
//    }
//
//    @PostMapping("/posts")
//    public ResponseEntity<ImageResponse> uploadPostImage() {
//
//    }
}
