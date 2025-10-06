package com.project.community.service;

import com.project.community.dto.PostResponseDto;
import com.project.community.dto.request.PostRequest;
import com.project.community.dto.request.PostUpdateRequest;
import com.project.community.entity.Post;
import com.project.community.entity.User;
import com.project.community.repository.PostRepository;
import com.project.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
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
        User writer = userRepository.findById(postRequest.getWriterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        Post post = PostMapper.toPost(postRequest, writer);
        postRepository.save(post);
        return PostMapper.toResponseDto(post);
    }

    public PostResponseDto getPostDetail(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found"));
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
        Post post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found"));
        post.setTitle(postUpdateRequest.getTitle());
        post.setContents(postUpdateRequest.getContents());
        postRepository.save(post);
        return PostMapper.toResponseDto(post);
    }
}
