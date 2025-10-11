package com.project.community.controller;

import com.project.community.dto.request.PostLikeRequest;
import com.project.community.dto.response.PostLikeResponse;
import com.project.community.service.PostLikeService;
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
                .body(PostLikeResponse.from("Like registered."));
    }

//    @GetMapping
//    public ResponseEntity<PostLikeResponse> registerLike(@PathVariable Long postId, @RequestBody PostLikeRequest postLikeRequest) {
//        postLikeService.registerPostLike(postId,postLikeRequest);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(PostLikeResponse.from("Like registered."));
//    }

    @DeleteMapping
    public ResponseEntity<PostLikeResponse> cancelLike(@PathVariable Long postId, @RequestBody PostLikeRequest likeRequest) {
        postLikeService.cancelPostLike(postId, likeRequest);
        return ResponseEntity.ok(PostLikeResponse.from("like canceled."));
    }
}
