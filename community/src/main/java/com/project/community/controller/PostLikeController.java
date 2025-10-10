package com.project.community.controller;

import com.project.community.dto.request.PostLikeRequest;
import com.project.community.dto.response.PostLikeResponse;
import com.project.community.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<PostLikeResponse> registerLike(@RequestBody PostLikeRequest postLikeRequest) {
        postLikeService.registerPostLike(postLikeRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PostLikeResponse.from("Like registered."));
    }

//    @DeleteMapping
//    public ResponseEntity<LikeResponse> cancelLike(@RequestBody LikeRequest likeRequest) {
//        likeService.cancelLike(likeRequest);
//        return ResponseEntity.ok(LikeResonse.from("like canceled."));
//    }
}
