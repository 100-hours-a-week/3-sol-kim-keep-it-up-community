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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public CommentResponseDto createComment(HttpServletRequest request, CommentPostRequest commentPostRequest) {
        HttpSession session = request.getSession();
        if (session == null) throw new CustomException(ErrorCode.SIGNIN_NEEDED);
        Long userId = (Long) session.getAttribute("userId");

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

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
    public CommentResponseDto updateComment(HttpServletRequest request, Long commentId, CommentUpdateRequest commentUpdateRequest) {
        HttpSession session = request.getSession();
        if (session == null) throw new CustomException(ErrorCode.SIGNIN_NEEDED);

        Long userId = (Long) session.getAttribute("userId");

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (comment.isDeleted()) throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);

        if (!comment.getWriter().getId().equals(userId)) throw new CustomException(ErrorCode.WRITER_ONLY_EDIT);

        comment.setContents(commentUpdateRequest.getContents());
        commentRepository.save(comment);
        return CommentMapper.toResponseDto(comment);
    }

    @Transactional
    public void deleteComment(HttpServletRequest request, Long commentId) {
        HttpSession session = request.getSession();
        if (session == null) throw new CustomException(ErrorCode.SIGNIN_NEEDED);

        Long userId = (Long) session.getAttribute("userId");

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (comment.isDeleted()) throw new CustomException(ErrorCode.COMMENT_ALREADY_DELETED);
        if (!comment.getWriter().getId().equals(userId)) throw new CustomException(ErrorCode.WRITER_ONLY_DELETE);

        comment.setDeleted(true);

        Post post = postRepository.findById(comment.getPost().getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_DELETED);
        
        post.deleteComment(comment);
    }
}


