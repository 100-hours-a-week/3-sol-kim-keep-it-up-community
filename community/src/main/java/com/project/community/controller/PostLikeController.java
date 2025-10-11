package com.project.community.controller;

import com.project.community.dto.PostLikeResponseDto;
import com.project.community.dto.request.PostLikeRequest;
import com.project.community.dto.response.PostLikeResponse;
import com.project.community.service.PostLikeService;
import com.project.community.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<PostLikeResponse> registerLike(@PathVariable Long postId, @RequestBody PostLikeRequest postLikeRequest) {
        postLikeService.registerPostLike(postId,postLikeRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PostLikeResponse.from(Message.POST_LIKE_REGISTERED.getMessage()));
    }

    @GetMapping
    public ResponseEntity<PostLikeResponse> getIsLiked(@PathVariable Long postId, @RequestBody PostLikeRequest postLikeRequest) {
        PostLikeResponseDto postLikeResponseDto = postLikeService.getIsPostLiked(postId, postLikeRequest);
        return ResponseEntity.ok(PostLikeResponse.from(Message.POST_LIKE_IS_LIKED_FETCHED.getMessage(), postLikeResponseDto));
    }

    @DeleteMapping
    public ResponseEntity<PostLikeResponse> cancelLike(@PathVariable Long postId, @RequestBody PostLikeRequest likeRequest) {
        postLikeService.cancelPostLike(postId, likeRequest);
        return ResponseEntity.ok(PostLikeResponse.from(Message.POST_LIKE_CANCELED.getMessage()));
    }
}
