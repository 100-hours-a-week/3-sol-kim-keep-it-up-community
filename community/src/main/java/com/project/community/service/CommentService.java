package com.project.community.service;

import com.project.community.common.CustomException;
import com.project.community.common.ErrorCode;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = userRepository.findById(commentPostRequest.getWriterId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(commentPostRequest.getPostId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_DELETED);

        Comment comment = CommentMapper.toComment(user, post, commentPostRequest);
        post.addComment(comment);
        commentRepository.save(comment);
        postRepository.save(post);
        return CommentMapper.toResponseDto(comment);
    }

    public List<CommentResponseDto> getPostComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_DELETED);
        List<Comment> commentList = post.getCommentList();
        return commentList.stream().map(c -> CommentMapper.toResponseDto(c)).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (comment.isDeleted()) throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        comment.setContents(commentUpdateRequest.getContents());
        commentRepository.save(comment);
        return CommentMapper.toResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (comment.isDeleted()) throw new CustomException(ErrorCode.COMMENT_ALREADY_DELETED);
        comment.setDeleted(true);
        Post post = postRepository.findById(comment.getPost().getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_DELETED);
        post.deleteComment(comment);
    }
}


