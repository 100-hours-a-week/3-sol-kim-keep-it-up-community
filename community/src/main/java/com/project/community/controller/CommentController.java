package com.project.community.controller;


import com.project.community.dto.CommentResponseDto;
import com.project.community.dto.request.CommentPostRequest;
import com.project.community.dto.request.CommentUpdateRequest;
import com.project.community.dto.response.CommentResponse;
import com.project.community.service.CommentService;
import com.project.community.common.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    /*
    POST 댓글 작성 v2
    => id, 작성자, 게시글 id, 내용, 작성일자
     */
    @PostMapping
    public ResponseEntity<CommentResponse> postComment(HttpServletRequest request, @PathVariable Long postId, @RequestBody CommentPostRequest commentPostRequestDto) {
        CommentResponseDto commentResponseDto = commentService.createComment(request, postId, commentPostRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommentResponse.from(Message.COMMENT_POST_SUCCESS.getMessage(), commentResponseDto));
    }

    /*
    GET 댓글 목록 조회
    => 댓글 List(id, 작성자, 게시글 id, 내용, 작성일자)
     */
    @GetMapping("/list")
    public ResponseEntity<CommentResponse> getPostComments(@PathVariable Long postId) {
        List<CommentResponseDto> commentResponseDtoList = commentService.getPostComments(postId);
        return ResponseEntity.ok(CommentResponse.from(Message.POST_COMMENT_FETCHED.getMessage(), commentResponseDtoList));
    }

    /*
    PATCH 댓글 수정
    => id, 작성자, 게시글 id, 내용, 작성일자
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(HttpServletRequest request, @PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {
        CommentResponseDto commentResponseDto = commentService.updateComment(request, commentId, commentUpdateRequest);
        return ResponseEntity.ok(CommentResponse.from(Message.COMMENT_UPDATED.getMessage(), commentResponseDto));
    }

    /*
    DELETE 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(HttpServletRequest request, @PathVariable Long commentId) {
        commentService.deleteComment(request, commentId);
        return ResponseEntity.ok(CommentResponse.from(Message.COMMENT_DELETED.getMessage()));
    }
}
