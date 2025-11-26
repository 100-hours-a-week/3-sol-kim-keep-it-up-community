package com.project.community.controller;

import com.project.community.dto.PostLikeResponseDto;
import com.project.community.dto.request.PostLikeRequest;
import com.project.community.dto.response.PostLikeResponse;
import com.project.community.service.PostLikeService;
import com.project.community.common.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    /*
    POST 게시글 좋아요 등록 v1
     */
    @PostMapping("/v1")
    public ResponseEntity<PostLikeResponse> registerLike(@PathVariable Long postId, @RequestBody PostLikeRequest postLikeRequest) {
        postLikeService.registerPostLike(postId, postLikeRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PostLikeResponse.from(Message.POST_LIKE_REGISTERED.getMessage()));
    }

    /*
    POST 게시글 좋아요 등록 v2
     */
    @PostMapping
    public ResponseEntity<PostLikeResponse> registerLike(HttpServletRequest request, @PathVariable Long postId) {
        postLikeService.registerPostLike(request, postId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PostLikeResponse.from(Message.POST_LIKE_REGISTERED.getMessage()));
    }

    /*
    GET 게시글 좋아요 여부 조회 v1
    => 좋아요 여부
     */
    @GetMapping("/v1/{userId}")
    public ResponseEntity<PostLikeResponse> getIsLiked(@PathVariable Long postId, @PathVariable Long userId) {
        PostLikeResponseDto postLikeResponseDto = postLikeService.getIsPostLiked(postId, userId);
        return ResponseEntity.ok(PostLikeResponse.from(Message.POST_LIKE_IS_LIKED_FETCHED.getMessage(), postLikeResponseDto));
    }

    /*
    GET 게시글 좋아요 여부 조회 v2
    => 좋아요 여부
     */
    @GetMapping
    public ResponseEntity<PostLikeResponse> getIsLiked(@PathVariable Long postId, HttpServletRequest request) {
        PostLikeResponseDto postLikeResponseDto = postLikeService.getIsPostLiked(postId, request);
        return ResponseEntity.ok(PostLikeResponse.from(Message.POST_LIKE_IS_LIKED_FETCHED.getMessage(), postLikeResponseDto));
    }

    /*
    DELETE 좋아요 취소 v1
     */
    @DeleteMapping("/v1")
    public ResponseEntity<PostLikeResponse> cancelLike(@PathVariable Long postId, @RequestBody PostLikeRequest likeRequest) {
        postLikeService.cancelPostLike(postId, likeRequest);
        return ResponseEntity.ok(PostLikeResponse.from(Message.POST_LIKE_CANCELED.getMessage()));
    }

    /*
    DELETE 좋아요 취소 v2
     */
    @DeleteMapping
    public ResponseEntity<PostLikeResponse> cancelLike(HttpServletRequest request, @PathVariable Long postId) {
        postLikeService.cancelPostLike(request, postId);
        return ResponseEntity.ok(PostLikeResponse.from(Message.POST_LIKE_CANCELED.getMessage()));
    }
}
