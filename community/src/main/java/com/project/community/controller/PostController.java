package com.project.community.controller;

import com.project.community.dto.PostResponseDto;
import com.project.community.dto.request.PostRequest;
import com.project.community.dto.request.PostUpdateRequest;
import com.project.community.dto.response.PostResponse;
import com.project.community.service.PostService;
import com.project.community.common.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /*
    POST, 게시글 작성 v2
    => id, 제목, 내용, 작성자, 작성일자, 조회수, 댓글 수, 좋아요 수
     */
    @PostMapping
    public ResponseEntity<PostResponse> publishPost(HttpServletRequest request, @Valid @RequestBody PostRequest postRequestDto) {
        PostResponseDto postResponseDto = postService.createPost(request, postRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PostResponse.from(Message.POST_PUBLISHED.getMessage(), postResponseDto));
    }

    /*
    GET, 게시글 조회
    => id, 제목, 내용, 작성자, 작성일자, 조회수, 댓글 수, 좋아요 수
     */
    @GetMapping("/detail/{postId}")
    public ResponseEntity<PostResponse> getPostDetail(@PathVariable Long postId) {
        PostResponseDto postResponseDto = postService.getPostDetail(postId);
        return ResponseEntity.ok(PostResponse.from(Message.POST_FETCHED.getMessage(), postResponseDto));
    }

    /*
    GET 게시글 목록 조회
    => 게시물 List(id, 제목, 내용, 작성자, 작성일자, 조회수, 댓글 수, 좋아요 수)
     */
    @GetMapping("/list")
    public ResponseEntity<PostResponse> getPostList(@RequestParam(required = false) Long cursorId,
                                                    @RequestParam(defaultValue = "20") int size) {
        Slice<PostResponseDto> postResponseDtoList = postService.getPostList(cursorId, size);
        return ResponseEntity.ok(PostResponse.from(Message.POST_LIST_FETCHED.getMessage(), postResponseDtoList));
    }

    /*
    PATCH 게시글 수정 v1
    => id, 제목, 내용, 작성자, 작성일자, 조회수, 댓글 수, 좋아요 수
     */
    @PatchMapping("/v1/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @Valid @RequestBody PostUpdateRequest postUpdateRequest) {
        PostResponseDto postResponseDto = postService.updatePost(postId, postUpdateRequest);
        return ResponseEntity.ok(PostResponse.from(Message.POST_UPDATE_SUCCESS.getMessage(), postResponseDto));
    }

    /*
    PATCH 게시글 수정 v2
    => id, 제목, 내용, 작성자, 작성일자, 조회수, 댓글 수, 좋아요 수
     */
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(HttpServletRequest request, @PathVariable Long postId, @Valid @RequestBody PostUpdateRequest postUpdateRequest) {
        PostResponseDto postResponseDto = postService.updatePost(request, postId, postUpdateRequest);
        return ResponseEntity.ok(PostResponse.from(Message.POST_UPDATE_SUCCESS.getMessage(), postResponseDto));
    }

    /*
    DELETE 게시글 삭제 v1
     */
    @DeleteMapping("/v1/{postId}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok(PostResponse.from(Message.POST_DELETE_SUCCESS.getMessage()));
    }

    /*
    DELETE 게시글 삭제 v2
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<PostResponse> deletePost(HttpServletRequest request, @PathVariable Long postId) {
        postService.deletePost(request, postId);
        return ResponseEntity.ok(PostResponse.from(Message.POST_DELETE_SUCCESS.getMessage()));
    }

    /*
    PATCH 조회수 증가
     */
    @PatchMapping("/{postId}/viewcount")
    public ResponseEntity<PostResponse> increaseViewsCount(@PathVariable Long postId) {
        PostResponseDto postResponseDto = postService.increaseViewsCount(postId);
        return ResponseEntity.ok(PostResponse.from(Message.POST_VIEW_COUNT_UPDATED.getMessage(), postResponseDto));
    }
}
