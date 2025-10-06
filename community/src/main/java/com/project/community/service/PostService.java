package com.project.community.service;

import com.project.community.dto.PostResponseDto;
import com.project.community.dto.request.PostRequest;
import com.project.community.entity.Post;
import com.project.community.entity.User;
import com.project.community.repository.PostRepository;
import com.project.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.project.community.util.PostMapper;
@Service
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;

    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequest postRequest) {
        User writer = userRepository.findById(postRequest.getWriterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        Post post = PostMapper.toPost(postRequest, writer);
        postRepository.save(post);
        return PostMapper.toResponseDto(post);
    }
}
