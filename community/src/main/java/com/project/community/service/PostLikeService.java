package com.project.community.service;

import com.project.community.dto.PostLikeResponseDto;
import com.project.community.dto.request.PostLikeRequest;
import com.project.community.entity.PostLike;
import com.project.community.entity.Post;
import com.project.community.entity.User;
import com.project.community.repository.PostLikeRepository;
import com.project.community.repository.UserRepository;
import com.project.community.repository.PostRepository;
import com.project.community.util.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void registerPostLike(Long postId, PostLikeRequest postLikeRequest) {
        User user = userRepository.findById(postLikeRequest.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND.getMessage()));
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.POST_NOT_FOUND.getMessage()));
        if (postLikeRepository.existsByUserIdAndPostId(postLikeRequest.getUserId(), postId)) throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.ALREADY_LIKED.getMessage());
        PostLike postLike = new PostLike(user, post);
        post.increaseLikesCount();
        postLikeRepository.save(postLike);
    }

    @Transactional
    public void cancelPostLike(Long postId, PostLikeRequest postLikeRequest) {
        User user = userRepository.findById(postLikeRequest.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND.getMessage()));
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.POST_NOT_FOUND.getMessage()));
        PostLike postLike = postLikeRepository.findByUserIdAndPostId(postLikeRequest.getUserId(), postId);
        post.decreaseLikesCount();
        postLikeRepository.delete(postLike);
    }

    public PostLikeResponseDto getIsPostLiked(Long postId, PostLikeRequest postLikeRequest) {
        PostLikeResponseDto postLikeResponseDto = new PostLikeResponseDto();
        if (postLikeRepository.existsByUserIdAndPostId(postLikeRequest.getUserId(), postId)) {
            postLikeResponseDto.setLiked(true);
        } else {
            postLikeResponseDto.setLiked(false);
        }
        return postLikeResponseDto;
    }
}
