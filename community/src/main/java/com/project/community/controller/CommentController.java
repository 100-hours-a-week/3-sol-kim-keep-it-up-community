package com.project.community.controller;


import com.project.community.dto.CommentResponseDto;
import com.project.community.dto.request.CommentPostRequest;
import com.project.community.dto.request.CommentUpdateRequest;
import com.project.community.dto.response.CommentResponse;
import com.project.community.service.CommentService;
import com.project.community.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> postComment(@RequestBody CommentPostRequest commentPostRequest) {
        CommentResponseDto commentResponseDto = commentService.createComment(commentPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommentResponse.from(Message.COMMENT_POST_SUCCESS.getMessage(), commentResponseDto));
    }

    @GetMapping()
    public ResponseEntity<CommentResponse> getPostComments(@PathVariable Long postId) {
        List<CommentResponseDto> commentResponseDtoList = commentService.getPostComments(postId);
        return ResponseEntity.ok(CommentResponse.from(Message.POST_COMMENT_FETCHED.getMessage(), commentResponseDtoList));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {
        CommentResponseDto commentResponseDto = commentService.updateComment(commentId, commentUpdateRequest);
        return ResponseEntity.ok(CommentResponse.from(Message.COMMENT_UPDATED.getMessage(), commentResponseDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(CommentResponse.from(Message.COMMENT_DELETED.getMessage()));
    }
}
