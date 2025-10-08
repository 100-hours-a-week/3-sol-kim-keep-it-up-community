package com.project.community.controller;


import com.project.community.dto.CommentResponseDto;
import com.project.community.dto.request.CommentPostRequest;
import com.project.community.dto.response.CommentResponse;
import com.project.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
