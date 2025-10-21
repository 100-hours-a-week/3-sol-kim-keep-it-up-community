package com.project.community.service;

import com.project.community.common.CustomException;
import com.project.community.common.ErrorCode;
import com.project.community.dto.PostLikeResponseDto;
import com.project.community.dto.request.PostLikeRequest;
import com.project.community.entity.PostLike;
import com.project.community.entity.Post;
import com.project.community.entity.User;
import com.project.community.repository.PostLikeRepository;
import com.project.community.repository.UserRepository;
import com.project.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void registerPostLike(Long postId, PostLikeRequest postLikeRequest) {
        User user = userRepository.findById(postLikeRequest.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_NOT_FOUND);
        if (postLikeRepository.existsByUserIdAndPostId(postLikeRequest.getUserId(), postId)) throw new CustomException(ErrorCode.ALREADY_LIKED);
        PostLike postLike = new PostLike(user, post);
        post.increaseLikesCount();
        postLikeRepository.save(postLike);
    }

    public PostLikeResponseDto getIsPostLiked(Long postId, Long userId) {
        PostLikeResponseDto postLikeResponseDto = new PostLikeResponseDto();
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_NOT_FOUND);
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        postLikeResponseDto.setLiked(postLikeRepository.existsByUserIdAndPostId(userId, postId));
        return postLikeResponseDto;
    }

    @Transactional
    public void cancelPostLike(Long postId, PostLikeRequest postLikeRequest) {
        User user = userRepository.findById(postLikeRequest.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_NOT_FOUND);
        PostLike postLike = postLikeRepository.findByUserIdAndPostId(user.getId(), postId);
        if (postLike == null) throw new CustomException(ErrorCode.NO_LIKE_TO_CANCEL);
        post.decreaseLikesCount();
        postLikeRepository.delete(postLike);
    }

}
