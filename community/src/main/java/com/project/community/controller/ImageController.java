package com.project.community.controller;

import com.project.community.dto.ImagePostResponseDto;
import com.project.community.dto.ImageResponseDto;
import com.project.community.dto.request.PostImageUploadRequest;
import com.project.community.dto.request.ProfileUploadRequest;
import com.project.community.dto.response.ImageResponse;
import com.project.community.service.ImageService;
import com.project.community.common.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /*
    POST 프로필 사진 등록
    => 사진 id
     */
    @PostMapping("/signUp/profiles")
    public ResponseEntity<ImageResponse> uploadProfileImage(HttpServletRequest httpServletRequest, @Valid @RequestBody ProfileUploadRequest requestDto) {
        ImagePostResponseDto imagePostResponseDto = imageService.uploadProfileImage(httpServletRequest, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ImageResponse.from(Message.PROFILE_IMAGE_POST_SUCCESS.getMessage(), imagePostResponseDto));
    }

    /*
    GET 프로필 사진 조회
    => 사진 url
     */
    @GetMapping("/profiles")
    public ResponseEntity<ImageResponse> getUserProfileImage(HttpServletRequest request) {
        ImageResponseDto imageResponseDto = imageService.getUserProfileImage(request);
        System.out.println(imageResponseDto);
        if (imageResponseDto == null) {
            System.out.println("imageResponseDto" + "no content");
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ImageResponse.from(Message.PROFILE_IMAGE_NOT_SET.getMessage()));
        }

        return ResponseEntity.ok(ImageResponse.from(Message.PROFILE_IMAGE_RETURNED.getMessage(), imageResponseDto));
    }

    /*
    PUT 프로필 사진 변경
    => 사진 id
     */
    @PutMapping("/profiles")
    public ResponseEntity<ImageResponse> updateProfileImage(HttpServletRequest httpServletRequest, @Valid @RequestBody ProfileUploadRequest requestDto) {
        ImagePostResponseDto imagePostResponseDto = imageService.updateUserProfileImage(httpServletRequest, requestDto);
        return ResponseEntity.ok(ImageResponse.from(Message.PROFILE_IMAGE_UPDATED.getMessage(), imagePostResponseDto));
    }

    /*
    POST 게시글 사진 등록
    => 사진 id
     */
    @PostMapping("/posts/{postId}")
    public ResponseEntity<ImageResponse> uploadPostImage(@Valid PostImageUploadRequest requestDto, @PathVariable Long postId) {
        ImagePostResponseDto imagePostResponseDto = imageService.uploadPostImage(requestDto, postId);
        return ResponseEntity.ok(ImageResponse.from(Message.POST_IMAGE_UPLOADED.getMessage(), imagePostResponseDto));
    }

    /*
    PUT 게시글 사진 변경
    => 사진 id
     */
    @PutMapping("/posts/{postId}")
    public ResponseEntity<ImageResponse> updatePostImage(HttpServletRequest httpServletRequest, @Valid @RequestBody PostImageUploadRequest requestDto, @PathVariable Long postId) {
        ImagePostResponseDto imagePostResponseDto = imageService.updatePostImage(httpServletRequest, requestDto, postId);
        return ResponseEntity.ok(ImageResponse.from(Message.POST_IMAGE_UPDATED.getMessage(), imagePostResponseDto));
    }
}
