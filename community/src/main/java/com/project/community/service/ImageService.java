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
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FileService fileService;

    /*
    프로필 사진 등록
     */
    @Transactional
    public ImagePostResponseDto uploadProfileImage(HttpServletRequest httpServletRequest, ProfileUploadRequest requestDto) {
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html
        HttpSession session = httpServletRequest.getSession(false);
        Long userId = (Long) session.getAttribute("userId");

        MultipartFile file = requestDto.getFile();
        Image image = fileService.uploadImage(file, userId, "profile");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setProfileImageUrl("images/" + image.getFilename());
        userRepository.save(user);

        imageRepository.save(image);
        return ImageMapper.toResponseDto(image);
    }

    /*
    프로필 사진 조회
     */
    public ImageResponseDto getUserProfileImage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("userId");
        Image profileImage = imageRepository.findByTypeAndUserId("profile", userId);
        if (profileImage == null) {
            return null;
        }
        String filename = profileImage.getFilename();
        return ImageMapper.toResponseDto("images/" + filename);
    }

    /*
    프로필 사진 변경
     */
    @Transactional
    public ImagePostResponseDto updateUserProfileImage(HttpServletRequest httpServletRequest, ProfileUploadRequest requestDto) {
        HttpSession session = httpServletRequest.getSession(false);

        Long userId = (Long) session.getAttribute("userId");
        Image prevImage = imageRepository.findByTypeAndUserId("profile", userId);
        if (prevImage != null) {
            prevImage.setUserId(null);
        }

        MultipartFile newFile = requestDto.getFile();
        Image image = fileService.uploadImage(newFile, userId, "profile");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setProfileImageUrl("images/" + image.getFilename());

        imageRepository.save(image);
        return ImageMapper.toResponseDto(image);
    }

    /*
    게시글 사진 등록
     */
    @Transactional
    public ImagePostResponseDto uploadPostImage(PostImageUploadRequest requestDto) {
        MultipartFile file = requestDto.getFile();
        Long postId = requestDto.getPostId();

        Image image = fileService.uploadImage(file, postId, "post");
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.setImageUrl("images/" + image.getFilename());

        imageRepository.save(image);
        postRepository.save(post);
        return ImageMapper.toResponseDto(image);
    }

    /*
    게시글 사진 변경
     */
    @Transactional
    public ImagePostResponseDto updatePostImage(HttpServletRequest httpServletRequest, PostImageUploadRequest requestDto) {
        HttpSession session = httpServletRequest.getSession(false);
        Long writerId = (Long) session.getAttribute("userId");

        MultipartFile newFile = requestDto.getFile();
        Long postId = requestDto.getPostId();
        Image prevImage = imageRepository.findByTypeAndPostId("post", postId);
        prevImage.setPostId(null);

        Image newImage = fileService.uploadImage(newFile, postId, "post");
        imageRepository.save(newImage);

        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!post.getWriter().getId().equals(writerId)) throw new CustomException(ErrorCode.WRITER_ONLY_CAN_EDIT);
        post.setImageUrl("images/" + newImage.getFilename());
        postRepository.save(post);

        return ImageMapper.toResponseDto(newImage);
    }
}
