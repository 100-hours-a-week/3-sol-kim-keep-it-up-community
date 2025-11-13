package com.project.community.service;

import com.project.community.common.CustomException;
import com.project.community.common.ErrorCode;
import com.project.community.dto.ImagePostResponseDto;
import com.project.community.dto.ImageResponseDto;
import com.project.community.dto.request.PostImageUploadRequest;
import com.project.community.dto.request.ProfileUploadRequest;
import com.project.community.entity.Image;
import com.project.community.entity.Post;
import com.project.community.entity.User;
import com.project.community.repository.ImageRepository;
import com.project.community.repository.PostRepository;
import com.project.community.repository.UserRepository;
import com.project.community.util.ImageMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /*
    프로필 사진 등록
     */
    @Transactional
    public ImagePostResponseDto uploadProfileImage(HttpServletRequest httpServletRequest, ProfileUploadRequest requestDto) {
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html
        Long userId = requestDto.getUserId();

        Image prevImage = imageRepository.findByTypeAndUserId("profile", userId);
        if (prevImage != null) {
            throw new CustomException(ErrorCode.PROFILE_IMAGE_ALREADY_SET);
        }

        String imageUrl = requestDto.getImageUrl();
        Image image = new Image(imageUrl, userId, "profile");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        imageRepository.save(image);
        return ImageMapper.toResponseDto(image);
    }

    /*
    프로필 사진 조회
     */
    public ImageResponseDto getUserProfileImage(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        Image profileImage = imageRepository.findByTypeAndUserId("profile", userId);

        if (profileImage == null) {
            return null;
        }
        String url = profileImage.getUrl();
        return ImageMapper.toResponseDto(url);
    }

    /*
    프로필 사진 변경
     */
    @Transactional
    public ImagePostResponseDto updateUserProfileImage(HttpServletRequest httpServletRequest, ProfileUploadRequest requestDto) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");

        Image prevImage = imageRepository.findByTypeAndUserId("profile", userId);
        if (prevImage != null) {
            prevImage.setUserId(null);
        }

        String imageUrl = requestDto.getImageUrl();
        Image image = new Image(imageUrl, userId, "profile");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setProfileImageUrl(imageUrl);

        imageRepository.save(image);
        return ImageMapper.toResponseDto(image);
    }

    /*
    게시글 사진 등록
     */
    @Transactional
    public ImagePostResponseDto uploadPostImage(PostImageUploadRequest requestDto, Long postId) {
        String imageUrl = requestDto.getImageUrl();

        Image image = new Image(imageUrl, postId, "post");
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.setImageUrl(imageUrl);

        imageRepository.save(image);
        postRepository.save(post);
        return ImageMapper.toResponseDto(image);
    }

    /*
    게시글 사진 변경
     */
    @Transactional
    public ImagePostResponseDto updatePostImage(HttpServletRequest httpServletRequest, PostImageUploadRequest requestDto, Long postId) {
        Long writerId = (Long)  httpServletRequest.getAttribute("userId");

        String imageUrl = requestDto.getImageUrl();
        Image prevImage = imageRepository.findByTypeAndPostId("post", postId);
        if (prevImage != null) {
            prevImage.setPostId(null);
        }

        Image newImage = new Image(imageUrl, postId, "post");
        imageRepository.save(newImage);

        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!post.getWriter().getId().equals(writerId)) throw new CustomException(ErrorCode.WRITER_ONLY_CAN_EDIT);
        post.setImageUrl(imageUrl);
        postRepository.save(post);

        return ImageMapper.toResponseDto(newImage);
    }
}
