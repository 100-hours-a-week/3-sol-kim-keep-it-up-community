package com.project.community.controller;

import com.project.community.dto.PostResponseDto;
import com.project.community.dto.request.PostRequest;
import com.project.community.dto.request.PostUpdateRequest;
import com.project.community.dto.response.PostResponse;
import com.project.community.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> publishPost(@Valid @RequestBody PostRequest postRequest) {
        PostResponseDto postResponseDto = postService.createPost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PostResponse.from("post published", postResponseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostDetail(@PathVariable Long id) {
        PostResponseDto postResponseDto = postService.getPostDetail(id);
        return ResponseEntity.ok(PostResponse.from("post info fetched", postResponseDto));
    }

    @GetMapping
    public ResponseEntity<PostResponse> getPostList() {
        List<PostResponseDto> postResponseDtoList = postService.getPostList();
        return ResponseEntity.ok(PostResponse.from("post list fetched", postResponseDtoList));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest postUpdateRequest) {
        PostResponseDto postResponseDto = postService.updatePost(id, postUpdateRequest);
        return ResponseEntity.ok(PostResponse.from("post update success", postResponseDto));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<PostResponse> deletePost() {
//
//    }
}
