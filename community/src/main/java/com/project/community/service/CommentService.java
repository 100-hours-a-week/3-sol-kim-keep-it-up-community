package com.project.community.service;

import com.project.community.dto.CommentResponseDto;
import com.project.community.dto.request.CommentPostRequest;
import com.project.community.dto.request.CommentUpdateRequest;
import com.project.community.entity.Comment;
import com.project.community.entity.Post;
import com.project.community.entity.User;
import com.project.community.repository.CommentRepository;
import com.project.community.repository.PostRepository;
import com.project.community.repository.UserRepository;
import com.project.community.util.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto createComment(CommentPostRequest commentPostRequest) {
        User user = userRepository.findById(commentPostRequest.getWriterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Post post = postRepository.findById(commentPostRequest.getPostId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        if (post.isDeleted()) throw new ResponseStatusException(HttpStatus.GONE, "Post has been deleted");

        Comment comment = CommentMapper.toComment(user, post, commentPostRequest);
        post.addComment(comment);
        commentRepository.save(comment);
        postRepository.save(post);
        return CommentMapper.toResponseDto(comment);
    }

    public List<CommentResponseDto> getPostComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found."));
        if (post.isDeleted()) throw new ResponseStatusException(HttpStatus.GONE, "Post has been deleted.");
        List<Comment> commentList = post.getCommentList();
        return commentList.stream().map(c -> CommentMapper.toResponseDto(c)).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found."));
        comment.setContents(commentUpdateRequest.getContents());
        commentRepository.save(comment);
        return CommentMapper.toResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found."));
        if (comment.isDeleted()) throw new ResponseStatusException(HttpStatus.GONE, "Already deleted comment");
        comment.setDeleted(true);
        Post post = postRepository.findById(comment.getPost().getId()).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (post.isDeleted()) throw new ResponseStatusException(HttpStatus.GONE, "Post has been deleted");
        post.deleteComment(comment);
    }
}


