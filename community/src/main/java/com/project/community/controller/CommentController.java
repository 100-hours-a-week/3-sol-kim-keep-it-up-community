package com.project.community.controller;


import com.project.community.dto.CommentResponseDto;
import com.project.community.dto.request.CommentPostRequest;
import com.project.community.dto.request.CommentUpdateRequest;
import com.project.community.dto.response.CommentResponse;
import com.project.community.service.CommentService;
import com.project.community.common.Message;
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

    /*
    POST 댓글 작성
    => id, 작성자, 게시글 id, 내용, 작성일자
     */
    @PostMapping
    public ResponseEntity<CommentResponse> postComment(@RequestBody CommentPostRequest commentPostRequest) {
        CommentResponseDto commentResponseDto = commentService.createComment(commentPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommentResponse.from(Message.COMMENT_POST_SUCCESS.getMessage(), commentResponseDto));
    }

    /*
    GET 댓글 목록 조회
    => 댓글 List(id, 작성자, 게시글 id, 내용, 작성일자)
     */
    @GetMapping()
    public ResponseEntity<CommentResponse> getPostComments(@PathVariable Long postId) {
        List<CommentResponseDto> commentResponseDtoList = commentService.getPostComments(postId);
        return ResponseEntity.ok(CommentResponse.from(Message.POST_COMMENT_FETCHED.getMessage(), commentResponseDtoList));
    }

    /*
    PATCH 댓글 수정
    => id, 작성자, 게시글 id, 내용, 작성일자
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {
        CommentResponseDto commentResponseDto = commentService.updateComment(commentId, commentUpdateRequest);
        return ResponseEntity.ok(CommentResponse.from(Message.COMMENT_UPDATED.getMessage(), commentResponseDto));
    }

    /*
    DELETE 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(CommentResponse.from(Message.COMMENT_DELETED.getMessage()));
    }
}
