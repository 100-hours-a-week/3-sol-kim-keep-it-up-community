package com.project.community.controller;

import com.project.community.dto.ImagePostResponseDto;
import com.project.community.dto.ImageResponseDto;
import com.project.community.dto.request.PostImageUploadRequest;
import com.project.community.dto.request.ProfileUploadRequest;
import com.project.community.dto.response.ImageResponse;
import com.project.community.service.ImageService;
import com.project.community.common.Message;
import jakarta.validation.Valid;
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
        System.out.println(imageResponseDto);
        if (imageResponseDto == null) {
            System.out.println("imageResponseDto" + "no content");
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ImageResponse.from(Message.PROFILE_IMAGE_NOT_SET.getMessage()));
        }

        return ResponseEntity.ok(ImageResponse.from(Message.PROFILE_IMAGE_RETURNED.getMessage(), imageResponseDto));
    }

    @PutMapping("/profiles")
    public ResponseEntity<ImageResponse> updateProfileImage(@Valid ProfileUploadRequest request) {
        ImagePostResponseDto imagePostResponseDto = imageService.updateUserProfileImage(request);
        return ResponseEntity.ok(ImageResponse.from(Message.PROFILE_IMAGE_UPDATED.getMessage(), imagePostResponseDto));
    }

    @PostMapping("/posts")
    public ResponseEntity<ImageResponse> uploadPostImage(@Valid PostImageUploadRequest request) {
        ImagePostResponseDto imagePostResponseDto = imageService.uploadPostImage(request);
        return ResponseEntity.ok(ImageResponse.from(Message.POST_IMAGE_UPLOADED.getMessage(), imagePostResponseDto));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ImageResponse> getPostImage(@PathVariable Long postId) {
        ImageResponseDto imageResponseDto = imageService.getPostImage(postId);
        if (imageResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ImageResponse.from(Message.POST_IMAGE_NOT_SET.getMessage()));
        }

        return ResponseEntity.ok(ImageResponse.from(Message.POST_IMAGE_RETURNED.getMessage(), imageResponseDto));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<ImageResponse> updatePostImage(@Valid PostImageUploadRequest request) {
        ImagePostResponseDto imagePostResponseDto = imageService.updatePostImage(request);
        return ResponseEntity.ok(ImageResponse.from(Message.POST_IMAGE_UPDATED.getMessage(), imagePostResponseDto));
    }
}
