package com.project.community.service;

import com.project.community.common.CustomException;
import com.project.community.common.ErrorCode;
import com.project.community.dto.PostResponseDto;
import com.project.community.dto.request.PostRequest;
import com.project.community.dto.request.PostUpdateRequest;
import com.project.community.entity.Post;
import com.project.community.entity.User;
import com.project.community.repository.PostRepository;
import com.project.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.community.util.PostMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final UserRepository userRepository;

    private final PostRepository postRepository;

    @Transactional
    public PostResponseDto createPost(PostRequest postRequest) {
        User writer = userRepository.findById(postRequest.getWriterId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = PostMapper.toPost(postRequest, writer);
        postRepository.save(post);
        return PostMapper.toResponseDto(post);
    }

    public PostResponseDto getPostDetail(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_NOT_FOUND);
        return PostMapper.toResponseDto(post);
    }

    public List<PostResponseDto> getPostList() {
        return  postRepository.findAllByIsDeletedFalse()
                .stream()
                .map(p -> PostMapper.toResponseDto(p))
                .toList();
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_NOT_FOUND);
        post.setTitle(postUpdateRequest.getTitle());
        post.setContents(postUpdateRequest.getContents());
        postRepository.save(post);
        return PostMapper.toResponseDto(post);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.setDeleted(true);
        postRepository.save(post);
    }
}
