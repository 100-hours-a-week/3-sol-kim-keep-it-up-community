package com.project.community.controller;

import com.project.community.dto.PostResponseDto;
import com.project.community.dto.request.PostRequest;
import com.project.community.dto.request.PostUpdateRequest;
import com.project.community.dto.response.PostResponse;
import com.project.community.service.PostService;
import com.project.community.util.Message;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
                .body(PostResponse.from(Message.POST_PUBLISHED.getMessage(), postResponseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostDetail(@PathVariable Long id) {
        PostResponseDto postResponseDto = postService.getPostDetail(id);
        return ResponseEntity.ok(PostResponse.from(Message.POST_FETCHED.getMessage(), postResponseDto));
    }

    @GetMapping
    public ResponseEntity<PostResponse> getPostList(@RequestParam(required = false) Long cursorId,
                                                    @RequestParam(defaultValue = "20") int size) {
        Slice<PostResponseDto> postResponseDtoList = postService.getPostList(cursorId, size);
        return ResponseEntity.ok(PostResponse.from(Message.POST_LIST_FETCHED.getMessage(), postResponseDtoList));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest postUpdateRequest) {
        PostResponseDto postResponseDto = postService.updatePost(id, postUpdateRequest);
        return ResponseEntity.ok(PostResponse.from(Message.POST_UPDATE_SUCCESS.getMessage(), postResponseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(PostResponse.from(Message.POST_DELETE_SUCCESS.getMessage()));
    }

    @PatchMapping("/{id}/viewcount")
    public ResponseEntity<PostResponse> increaseViewsCount(@PathVariable Long id) {
        PostResponseDto postResponseDto = postService.increaseViewsCount(id);
        return ResponseEntity.ok(PostResponse.from(Message.POST_VIEW_COUNT_UPDATED.getMessage(), postResponseDto));
    }
}
