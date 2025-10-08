package com.project.community.controller;


import com.project.community.dto.CommentResponseDto;
import com.project.community.dto.request.CommentPostRequest;
import com.project.community.dto.response.CommentResponse;
import com.project.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> postComment(@RequestBody CommentPostRequest commentPostRequest) {
        CommentResponseDto commentResponseDto = commentService.createComment(commentPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommentResponse.from("Comment posted", commentResponseDto));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CommentResponse> getPostComments(@PathVariable Long postId) {
        List<CommentResponseDto> commentResponseDtoList = commentService.getPostComments(postId);
        return ResponseEntity.ok(CommentResponse.from("Post's comments fetched", commentResponseDtoList));
    }

}
